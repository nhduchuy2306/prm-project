package com.example.gearmobile.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.gearmobile.R;
import com.example.gearmobile.activities.LoginActivity;

public class ProfileFragment extends Fragment {

    private LinearLayout linearLayout;
    private Button buttonLogout;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        linearLayout = view.findViewById(R.id.profile_area);
        buttonLogout=view.findViewById(R.id.logoutButton);
        buttonLogout.setOnClickListener(v -> logout());
        /*buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/

        return view;
    }
    /*private void logout() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        // Chuyển người dùng trở lại màn hình đăng nhập
        Intent intent = new Intent(ProfileFragment.this, LoginActivity.class);
        startActivity(intent);
        finish(); // Tùy chọn: Đóng Activity hiện tại sau khi chuyển sang Activity mới
    }*/

    private void navigateToLoginActivity() {
        // Create an intent to launch the login activity
        Intent intent = new Intent(requireActivity(), LoginActivity.class);

        // Clear the back stack, so the user cannot navigate back to the logout fragment
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        // Start the login activity
        startActivity(intent);

        // Finish the current activity (logout fragment)
        requireActivity().finish();
    }

    private void logout() {
        // Clear the user session or perform any necessary logout operations

        // Access the SharedPreferences
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        // Edit the SharedPreferences using an editor
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Clear the saved user data or any relevant data
        editor.clear();

        // Commit the changes
        editor.apply();
        Log.d("LogOut","Oke");
        // Navigate to the login screen or perform any necessary UI updates
        // For example, you can use a navigation component to navigate to the login fragment
        navigateToLoginActivity();
    }

}
