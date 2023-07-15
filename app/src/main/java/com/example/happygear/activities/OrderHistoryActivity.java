package com.example.happygear.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.happygear.MainActivity;
import com.example.happygear.R;
import com.example.happygear.adapters.OrderHistoryAdapter;
import com.example.happygear.databases.AppDatabase;
import com.example.happygear.dto.CartDto;
import com.example.happygear.dto.OrderDetailModel;
import com.example.happygear.interfaces.OrderHistoryListener;
import com.example.happygear.services.UserService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderHistoryActivity extends AppCompatActivity implements OrderHistoryListener {

    private RecyclerView rcvOrderHistoryRecyclerView;
    private OrderHistoryAdapter orderHistoryAdapter;
    private List<OrderDetailModel> orderDetailModels;
    private AppDatabase db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history_profile);

        db = Room.databaseBuilder(this, AppDatabase.class, "cart.db").allowMainThreadQueries().build();

        Toolbar toolbar = findViewById(R.id.order_history_toolbar);
        toolbar.setNavigationOnClickListener(v -> {
            onBackPressed();
        });

        rcvOrderHistoryRecyclerView = findViewById(R.id.rcv_order_history);
        orderHistoryAdapter = new OrderHistoryAdapter(this);

        LinearLayoutManager LinearLayoutmanager = new LinearLayoutManager(this);
        rcvOrderHistoryRecyclerView.setLayoutManager(LinearLayoutmanager);

        loadOrderHistory();
        rcvOrderHistoryRecyclerView.setAdapter(orderHistoryAdapter);
    }

    private void loadOrderHistory() {
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");

        UserService.userService.getAllOrderDetailsByUsername(username).enqueue(new Callback<List<OrderDetailModel>>() {
            @Override
            public void onResponse(Call<List<OrderDetailModel>> call, Response<List<OrderDetailModel>> response) {
                if (response.isSuccessful()) {
                    orderDetailModels = response.body();
                    orderHistoryAdapter.setmOrderDetailModelList(orderDetailModels);
                }
            }

            @Override
            public void onFailure(Call<List<OrderDetailModel>> call, Throwable t) {
                Log.e("Error", t.getMessage());
            }
        });
    }

    private void updateCartInMainActivity(int cartSize) {
        Intent intent = new Intent(MainActivity.ACTION_UPDATE_CART_BADGE);
        intent.putExtra("cartSize", cartSize);
        sendBroadcast(intent);
    }

    @Override
    public void onOrderHistoryClick(OrderDetailModel orderDetailModel) {
        CartDto existCart = db.cartDao().getCartItem(orderDetailModel.getProductId());
        if (existCart != null) {
            existCart.setQuantity(existCart.getQuantity() + 1);
            new Thread(() -> {
                db.cartDao().update(existCart.getProductId(), existCart.getQuantity());
            }).start();
            return;
        }
        CartDto cartDto = new CartDto(
                orderDetailModel.getProductId(),
                1,
                orderDetailModel.getPrice(),
                orderDetailModel.getProductName(),
                orderDetailModel.getPicture()
        );

        new Thread(() -> {
            db.cartDao().insert(cartDto);
        }).start();

        Toast.makeText(OrderHistoryActivity.this, "Added to cart", Toast.LENGTH_SHORT).show();

        new Thread(() -> {
            int cartSize = db.cartDao().getCartCount();
            updateCartInMainActivity(cartSize);
        }).start();
    }
}
