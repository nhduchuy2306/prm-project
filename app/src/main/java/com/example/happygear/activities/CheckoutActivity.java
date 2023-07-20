package com.example.happygear.activities;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.happygear.MainActivity;
import com.example.happygear.R;
import com.example.happygear.adapters.CheckOutAdapter;
import com.example.happygear.databases.AppDatabase;
import com.example.happygear.dto.CartDto;
import com.example.happygear.dto.CheckoutDto;
import com.example.happygear.dto.CreateOrderDto;
import com.example.happygear.models.User;
import com.example.happygear.services.OrderService;
import com.example.happygear.utils.SerializableObject;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.momo.momo_partner.AppMoMoLib;

public class CheckoutActivity extends AppCompatActivity {

    private RecyclerView checkoutRecyclerView;
    private CheckOutAdapter checkOutAdapter;
    private TextView checkoutTotal;
    private Button checkoutProcessButton;
    private ImageView checkoutMomoButton;
    private List<CheckoutDto> checkoutDtoList;
    private AppDatabase db;
    private SharedPreferences sharedPreferences;
    private User user;
    private GoogleSignInAccount account;
    private Double total = 0.0;

    // MoMo
    private String amount = "10000";
    private String merchantName = "Happy Gear";
    private String merchantCode = "";
    private String description = "Thanh toán dịch vụ Happy Gear";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        Toolbar toolbar = findViewById(R.id.toolbar_checkout);
        toolbar.setNavigationOnClickListener(v -> {
            onBackPressed();
        });

        try {
            AppMoMoLib.getInstance().setEnvironment(AppMoMoLib.ENVIRONMENT.DEVELOPMENT); // AppMoMoLib.ENVIRONMENT.PRODUCTION
            db = Room.databaseBuilder(this, AppDatabase.class, "cart.db").allowMainThreadQueries().build();
            account = GoogleSignIn.getLastSignedInAccount(this);
            sharedPreferences = this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            String userSerialized = sharedPreferences.getString("user", null);
            if (userSerialized != null) {
                user = (User) SerializableObject.deserializeObject(userSerialized);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        checkoutTotal = findViewById(R.id.tv_total_checkout);
        checkoutProcessButton = findViewById(R.id.btn_checkout_process);
        checkoutMomoButton = findViewById(R.id.img_momo);

        checkoutRecyclerView = findViewById(R.id.recycler_view_checkout);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        checkoutRecyclerView.setLayoutManager(linearLayoutManager);

        checkOutAdapter = new CheckOutAdapter();

        checkoutDtoList = new ArrayList<>();

        checkoutDtoList.clear();
        for (CartDto cartDto : getCartDtoList()) {
            total += cartDto.getQuantity() * cartDto.getPrice();
            CheckoutDto checkoutDto = new CheckoutDto(
                    cartDto.getProductName(),
                    cartDto.getQuantity() * cartDto.getPrice(),
                    cartDto.getQuantity()
            );
            checkoutDtoList.add(checkoutDto);
        }

        checkOutAdapter.setDataCheckout(checkoutDtoList);
        checkoutRecyclerView.setAdapter(checkOutAdapter);
        String formattedNumber = String.valueOf(total).replaceAll("\\.0+$", "");
        checkoutTotal.setText("$" + formattedNumber);

        checkoutProcessButton.setOnClickListener(v -> checkOut());
        checkoutMomoButton.setOnClickListener(v -> requestPayment());

    }

    private void checkOut() {
        String username = "";
        if (account != null) {
            username = account.getId();
        } else if (user != null) {
            username = user.getUsername();
        }

        if (username.equals("")) {
            Toast.makeText(this, "Please login to continue", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d("CartFragment", username);

        CreateOrderDto createOrderDto = new CreateOrderDto(username, getCartDtoList());

        OrderService.orderService.createOrder(createOrderDto).enqueue(new Callback<String>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    String orderId = response.body();
                    Log.d("CartFragment", orderId);
                    Log.d("CartFragment", "Order created successfully");
                    new Thread(() -> {
                        db.cartDao().clearCart();
                        int cartSize = db.cartDao().getCartCount();
                        updateCartInMainActivity(cartSize);
                    }).start();
                    checkoutDtoList.clear();
                    checkOutAdapter.notifyDataSetChanged();
                    sendSuccessOrderNotification();

                    Toast.makeText(CheckoutActivity.this, "Order created successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("CartFragment", t.getMessage());
            }
        });
    }

    private void sendSuccessOrderNotification() {
        Notification notification = new NotificationCompat.Builder(this, getString(R.string.channel_id))
                .setContentTitle("Order created successfully")
                .setContentText("Your order has been created successfully")
                .setSmallIcon(R.drawable.giphy)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.giphy))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .build();

        NotificationManager notificationManager = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify(randomId(), notification);
        }
    }

    private int randomId() {
        Date date = new Date();
        return (int) date.getTime();
    }

    private List<CartDto> getCartDtoList() {
        return db.cartDao().getCartItems();
    }

    private void updateCartInMainActivity(int cartSize) {
        Intent intent = new Intent(MainActivity.ACTION_UPDATE_CART_BADGE);
        intent.putExtra("cartSize", cartSize);
        sendBroadcast(intent);
    }

    //Get token through MoMo app
    private void requestPayment() {
        AppMoMoLib.getInstance().setAction(AppMoMoLib.ACTION.PAYMENT);
        AppMoMoLib.getInstance().setActionType(AppMoMoLib.ACTION_TYPE.GET_TOKEN);

        Map<String, Object> eventValue = new HashMap<>();
        //client Required
        eventValue.put("merchantname", merchantName);
        eventValue.put("merchantcode", merchantCode);
        eventValue.put("amount", total * 23000);
        eventValue.put("orderId", UUID.randomUUID().toString());
        eventValue.put("orderLabel", "Mã đơn hàng");

        //client Optional - bill info
        eventValue.put("merchantnamelabel", "Dịch vụ");
        eventValue.put("fee", 10); //Kiểu integer
        eventValue.put("description", description);

        //client extra data
        eventValue.put("requestId", merchantCode + "merchant_billId_" + System.currentTimeMillis());
        eventValue.put("partnerCode", merchantCode);
        eventValue.put("extraData", "");
        eventValue.put("extra", "");
        AppMoMoLib.getInstance().requestMoMoCallBack(this, eventValue);


    }

    //Get token callback from MoMo app an submit to server side
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppMoMoLib.getInstance().REQUEST_CODE_MOMO && resultCode == -1) {
            if (data != null) {
                if (data.getIntExtra("status", -1) == 0) {
                    //TOKEN IS AVAILABLE
                    String token = data.getStringExtra("data"); //Token response
                    Log.d("TOKEN", token);
                    String phoneNumber = data.getStringExtra("phonenumber");
                    String env = data.getStringExtra("env");
                    if (env == null) {
                        env = "app";
                    }

                    if (token != null && !token.equals("")) {
                        Log.d("TOKEN", "Received token " + token + ", phonenumber: " + phoneNumber + ", env: " + env);
                        checkOut();
                    } else {
                        Log.d("TOKEN", "not receive info");
                        Toast.makeText(this, "Payment Fail", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(this, MainActivity.class);
                        finish();
                        startActivity(intent);
                    }
                } else if (data.getIntExtra("status", -1) == 1) {
                    //TOKEN FAIL
                    String message = data.getStringExtra("message") != null ? data.getStringExtra("message") : "Thất bại";
                    Log.d("TOKEN", message);
                    Toast.makeText(this, "Payment Fail", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, MainActivity.class);
                    finish();
                    startActivity(intent);
                } else if (data.getIntExtra("status", -1) == 2) {
                    //TOKEN FAIL
                    Log.d("TOKEN", "Không thấy app momo");
                    Toast.makeText(this, "Payment Fail", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, MainActivity.class);
                    finish();
                    startActivity(intent);
                } else {
                    //TOKEN FAIL
                    Log.d("TOKEN", "Không thấy app momo");
                    Toast.makeText(this, "Payment Fail", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, MainActivity.class);
                    finish();
                    startActivity(intent);
                }
            } else {
                Log.d("TOKEN", "Không thấy app momo");
                Toast.makeText(this, "Payment Fail", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, MainActivity.class);
                finish();
                startActivity(intent);
            }
        } else {
            Log.d("TOKEN", "Không thấy app momo");
            Toast.makeText(this, "Payment Fail", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            finish();
            startActivity(intent);
        }
    }

}