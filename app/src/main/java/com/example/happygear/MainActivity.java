package com.example.happygear;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.happygear.fragments.CartFragment;
import com.example.happygear.fragments.ExploreFragment;
import com.example.happygear.fragments.HomeFragment;
import com.example.happygear.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView mBottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBottomNavigationView = findViewById(R.id.bottom_navigation);

        mBottomNavigationView.setSelectedItemId(R.id.navigation_home);
        replaceFragment(new HomeFragment());
        mBottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.navigation_home) {
                    replaceFragment(new HomeFragment());
                }
                else if(item.getItemId() == R.id.navigation_explore) {
                    replaceFragment(new ExploreFragment());
                }
                else if(item.getItemId() == R.id.navigation_cart) {
                    replaceFragment(new CartFragment());
                }
                else if(item.getItemId() == R.id.navigation_profile) {
                    replaceFragment(new ProfileFragment());
                }
                return true;
            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_main_fragment, fragment);
        fragmentTransaction.commit();
    }
}