package com.example.happygear.activities;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.room.Room;

import com.example.happygear.MainActivity;
import com.example.happygear.R;
import com.example.happygear.databases.AppDatabase;
import com.example.happygear.dto.CartDto;

import java.util.List;

public class SplashActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSION_CODE = 10;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        db = Room.databaseBuilder(this, AppDatabase.class, "cart.db").allowMainThreadQueries().build();

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permission already granted", Toast.LENGTH_SHORT).show();
        } else {
            if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(this, "Permission is needed to access location", Toast.LENGTH_SHORT).show();
            }

            String[] premissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.POST_NOTIFICATIONS};
            requestPermissions(premissions, REQUEST_PERMISSION_CODE);
        }

        new Thread(() -> {
            List<CartDto> cartDtoList = db.cartDao().getCartItems();
            if (cartDtoList.size() > 0) {
                sendNotification();
            }
        }).start();

        loadData();
    }

    private void loadData() {
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }, 3000);
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