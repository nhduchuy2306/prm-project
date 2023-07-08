package com.example.happygear;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.room.Room;

import com.example.happygear.databases.AppDatabase;
import com.example.happygear.dto.CartDto;
import com.example.happygear.fragments.CartFragment;
import com.example.happygear.fragments.ExploreFragment;
import com.example.happygear.fragments.HomeFragment;
import com.example.happygear.fragments.ProfileFragment;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView mBottomNavigationView;
    private static final int REQUEST_PERMISSION_CODE = 10;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = Room.databaseBuilder(this, AppDatabase.class, "cart.db").allowMainThreadQueries().build();

        mBottomNavigationView = findViewById(R.id.bottom_navigation);

        mBottomNavigationView.setSelectedItemId(R.id.navigation_home);
        replaceFragment(new HomeFragment());
        mBottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.navigation_home) {
                    replaceFragment(new HomeFragment());
                } else if (item.getItemId() == R.id.navigation_explore) {
                    replaceFragment(new ExploreFragment());
                } else if (item.getItemId() == R.id.navigation_cart) {
                    replaceFragment(new CartFragment());
                } else if (item.getItemId() == R.id.navigation_profile) {
                    replaceFragment(new ProfileFragment());
                }
                return true;
            }
        });

        new Handler(this.getMainLooper()).postDelayed(() -> {
            int size = db.cartDao().getCartCount();
            updateCartBadge(size);
        }, 1000);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }

        if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permission already granted", Toast.LENGTH_SHORT).show();
        } else {
            if (shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(this, "Permission is needed to access location", Toast.LENGTH_SHORT).show();
            }

            String[] premissions = {android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.POST_NOTIFICATIONS};
            requestPermissions(premissions, REQUEST_PERMISSION_CODE);
        }

        new Thread(() -> {
            List<CartDto> cartDtoList = db.cartDao().getCartItems();
            if (cartDtoList.size() > 0) {
                sendNotification();
            }
        }).start();
    }

    public void updateCartBadge(int cartSize) {
        BottomNavigationView mBottomNavigationView = findViewById(R.id.bottom_navigation);
        BadgeDrawable badgeDrawable = mBottomNavigationView.getOrCreateBadge(R.id.navigation_cart);

        if (cartSize > 0) {
            badgeDrawable.setVisible(true);
            badgeDrawable.setVerticalOffset(20);
            badgeDrawable.setNumber(cartSize);
            badgeDrawable.setBadgeTextColor(getResources().getColor(R.color.white));
            badgeDrawable.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        } else {
            badgeDrawable.setVisible(false);
            badgeDrawable.clearNumber();
        }
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_main_fragment, fragment);
        fragmentTransaction.commit();
    }

    private void sendNotification() {
        Notification notification = new NotificationCompat.Builder(this, getString(R.string.channel_id))
                .setContentTitle("Product In Cart")
                .setContentText("You have product in cart")
                .setSmallIcon(R.drawable.giphy)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.giphy))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify(R.string.channel_id, notification);
        }
    }
}