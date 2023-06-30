package com.example.gearmobile.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.example.gearmobile.MainActivity;
import com.example.gearmobile.R;
import com.example.gearmobile.utils.AppUtil;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        loadData();
    }

    private void loadData() {
        if(AppUtil.isNetworkAvailable(this)) {
            // Network available
            //load data

            // delay
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Go to main activity
                    //Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 3000);
        }
        else {
            // Network not available
            Toast.makeText(this, "Network disconnected", Toast.LENGTH_SHORT).show();
        }
    }
}