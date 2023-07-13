package com.example.happygear.activities;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.happygear.R;
import com.example.happygear.adapters.ShopAddressAdapter;
import com.example.happygear.interfaces.AddressListener;
import com.example.happygear.models.ShopAddress;
import com.example.happygear.services.ProductService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AboutActivity extends AppCompatActivity implements AddressListener {

    private List<ShopAddress> mShopAddressList;
    private RecyclerView addressRecyclerView;
    private ShopAddressAdapter mShopAddressAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Toolbar toolbar = findViewById(R.id.aboutus_toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        addressRecyclerView = findViewById(R.id.recyclerView_address);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        addressRecyclerView.setLayoutManager(linearLayoutManager);
        mShopAddressList = new ArrayList<>();
        loadShopAddress();
        mShopAddressAdapter = new ShopAddressAdapter(this);
    }

    private void loadShopAddress() {
        ProductService.productService.getShopAddress().enqueue(new Callback<List<ShopAddress>>() {
            @Override
            public void onResponse(Call<List<ShopAddress>> call, Response<List<ShopAddress>> response) {
                if (response.isSuccessful()) {
                    mShopAddressList = response.body();
                    mShopAddressAdapter.setData(mShopAddressList);
                    addressRecyclerView.setAdapter(mShopAddressAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<ShopAddress>> call, Throwable t) {

            }
        });
    }

    @Override
    public void onAddressClick(ShopAddress shopAddress) {
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("shopAddress", shopAddress);
        startActivity(intent);

    }
}