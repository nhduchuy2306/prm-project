package com.example.happygear.activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.happygear.R;

public class ChatManagementActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chatmanagement);


        backToPreviousScreen();
    }

    private void backToPreviousScreen(){
        Toolbar toolbar = findViewById(R.id.toolbar_chatmanagement);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }
}
