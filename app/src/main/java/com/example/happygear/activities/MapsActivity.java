package com.example.happygear.activities;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.happygear.R;
import com.example.happygear.models.ShopAddress;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap myMap;
    private ShopAddress shopAddress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        myMap = googleMap;

        loadShopAddress();
        LatLng myStore = new LatLng(Double.parseDouble(this.shopAddress.getLatitude()),
                Double.parseDouble(this.shopAddress.getLongitude()));
        myMap.addMarker(new MarkerOptions().position(myStore).title("Happy Gear," + this.shopAddress.getAddress()));
        myMap.moveCamera(CameraUpdateFactory.newLatLng(myStore));

        //to zoom map
        myMap.getUiSettings().setZoomControlsEnabled(true);
        myMap.getUiSettings().setCompassEnabled(true);

        Toolbar toolbar = findViewById(R.id.toolbar_map);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void loadShopAddress(){
        this.shopAddress= (ShopAddress) getIntent().getSerializableExtra("shopAddress");
    }
}
