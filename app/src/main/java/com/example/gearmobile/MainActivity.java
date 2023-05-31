package com.example.gearmobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.example.gearmobile.adapters.ViewPagerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView mBottomNavigationView;
    private ViewPager2 mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBottomNavigationView = findViewById(R.id.bottom_navigation);
        mViewPager = findViewById(R.id.view_pager);

        setupViewPager();

        mBottomNavigationView.setOnItemSelectedListener(item -> {
            if(item.getItemId() == R.id.navigation_home) {
                mViewPager.setCurrentItem(0);
            }
            else if(item.getItemId() == R.id.navigation_explore) {
                mViewPager.setCurrentItem(1);
            }
            else if(item.getItemId() == R.id.navigation_cart) {
                mViewPager.setCurrentItem(2);
            }
            else if(item.getItemId() == R.id.navigation_chat) {
                mViewPager.setCurrentItem(3);
            }
            else {
                mViewPager.setCurrentItem(4);
            }
            return true;
        });
    }

    private void setupViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        mViewPager.setAdapter(adapter);

        mViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                mBottomNavigationView.getMenu().getItem(position).setChecked(true);
            }
        });
    }
}