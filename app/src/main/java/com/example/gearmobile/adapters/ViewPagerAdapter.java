package com.example.gearmobile.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.gearmobile.fragments.CartFragment;
import com.example.gearmobile.fragments.ChatFragment;
import com.example.gearmobile.fragments.ExploreFragment;
import com.example.gearmobile.fragments.HomeFragment;
import com.example.gearmobile.fragments.ProfileFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {


    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
            Fragment fragment = null;
            switch(position) {
                case 1:
                    fragment = new ExploreFragment();
                    break;
                case 2:
                    fragment = new CartFragment();
                    break;
                case 3:
                    fragment = new ChatFragment();
                    break;
                case 4:
                    fragment = new ProfileFragment();
                    break;
                default:
                    fragment = new HomeFragment();
                    break;
            }
            return fragment;
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}
