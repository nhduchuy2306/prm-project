package com.example.gearmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if(item.getItemId() == R.id.navigation_home) {
                Toast.makeText(MainActivity.this, "Home Screen", Toast.LENGTH_SHORT).show();
            }
            else if(item.getItemId() == R.id.navigation_explore) {
                Toast.makeText(MainActivity.this, "Explore Screen", Toast.LENGTH_SHORT).show();
            }
            else if(item.getItemId() == R.id.navigation_cart) {
                Toast.makeText(MainActivity.this, "Cart Screen", Toast.LENGTH_SHORT).show();
            }
            else if(item.getItemId() == R.id.navigation_chat) {
                Toast.makeText(MainActivity.this, "Chat Screen", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(MainActivity.this, "Profile Screen", Toast.LENGTH_SHORT).show();
            }
            return true;
        });
    }
}