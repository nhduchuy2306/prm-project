package com.example.happygear.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.happygear.R;
import com.example.happygear.models.ShopAddress;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap myMap;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private LatLng myStore;
    private ShopAddress shopAddress;

    private ArrayList<MarkerOptions> markersList = new ArrayList<MarkerOptions>();


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
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                // Called when the user's location changes
//                myMap.clear(); // Clear existing markers

                LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                MarkerOptions userMarker = new MarkerOptions().position(userLocation).title("Your Location")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                addMarkShopAddress();
                myMap.addMarker(userMarker);

                PolylineOptions lineOptions = new PolylineOptions();
                lineOptions.add(userLocation);
                lineOptions.add(myStore);
                Polyline polyline = googleMap.addPolyline(lineOptions);
                polyline.setWidth(4);
                polyline.setColor(R.color.black);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {}

            @Override
            public void onProviderEnabled(String provider) {}

            @Override
            public void onProviderDisabled(String provider) {}
        };
        // Request location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        } else {
            // Request location permissions
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, 1);
        }
        for(int i = 0; i < this.markersList.size(); i++){
            myMap.addMarker(markersList.get(i));
        }

        //to zoom map
        myMap.getUiSettings().setZoomControlsEnabled(true);
        myMap.getUiSettings().setCompassEnabled(true);

        myMap.setMyLocationEnabled(true);
        //zoom when clicking
//        myMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
//            @Override
//            public boolean onMarkerClick(Marker marker) {
//                LatLng markerPosition = marker.getPosition();
//                myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(markerPosition, 20));
//                myMap.getUiSettings().setMapToolbarEnabled(true);
//                return true;
//            }
//        });

        //back previous page
        Toolbar toolbar = findViewById(R.id.toolbar_map);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void addMarkShopAddress(){
        this.shopAddress= (ShopAddress) getIntent().getSerializableExtra("shopAddress");
        this.myStore = new LatLng(Double.parseDouble(this.shopAddress.getLatitude()),
                Double.parseDouble(this.shopAddress.getLongitude()));
        MarkerOptions storeMarker = new MarkerOptions().position(myStore).title("Happy Gear," + this.shopAddress.getAddress());
        myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myStore, 10));
        myMap.addMarker(storeMarker);
//        return  myStore;
    }

}
