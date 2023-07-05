package com.example.happygear.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.happygear.R;
import com.example.happygear.adapters.OrderHistoryAdapter;
import com.example.happygear.models.Order;
import com.example.happygear.models.ProductPicture;
import com.example.happygear.models.User;
import com.example.happygear.services.ProductPictureService;
import com.example.happygear.services.UserService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderHistoryActivity extends AppCompatActivity {

    private User user;
    private RecyclerView rcvOrderHistoryRecyclerView;

    private OrderHistoryAdapter orderHistoryAdapter;
    private List<Order> mOrders;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history_profile);

        rcvOrderHistoryRecyclerView =findViewById(R.id.rcv_order_history);
        orderHistoryAdapter = new OrderHistoryAdapter(this);

        LinearLayoutManager LinearLayoutmanager = new LinearLayoutManager(this);
        rcvOrderHistoryRecyclerView.setLayoutManager(LinearLayoutmanager);

        loadOrderHistory();
        rcvOrderHistoryRecyclerView.setAdapter(orderHistoryAdapter);
    }

    private void loadOrderHistory() {
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        UserService.userService.getOrdersbyUsername(username).enqueue(
                new Callback<List<Order>>() {
                    @Override
                    public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                        if (response.isSuccessful()) {
                            mOrders = response.body();
                            orderHistoryAdapter.setOrderList(mOrders);
                            orderHistoryAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Order>> call, Throwable t) {
                        Log.d("OrderHistoryActivity", "onFailure: " + t.getMessage());
                    }
                }
        );
    }
}
