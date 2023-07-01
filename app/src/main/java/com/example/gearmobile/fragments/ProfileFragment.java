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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.gearmobile.MainActivity;
import com.example.gearmobile.R;
import com.example.gearmobile.activities.LoginActivity;
import com.example.gearmobile.models.User;
import com.example.gearmobile.utils.SerializableObject;

public class ProfileFragment extends Fragment {

    private LinearLayout profileAreaLayout;
    private TextView profileName;
    private TextView profileEmail;
    private Button buttonLogout;
    private Button buttonLogin;
    private SharedPreferences sharedPreferences;
    private User user;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            String userSerialized = sharedPreferences.getString("user", null);
            if (userSerialized != null) {
                user = (User) SerializableObject.deserializeObject(userSerialized);
            }
        } catch (Exception e) {
            Log.e("ProfileFragment", e.getMessage());
        }
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Initialize view
        profileAreaLayout = view.findViewById(R.id.profile_area);
        buttonLogin = view.findViewById(R.id.loginButton);
        buttonLogout = view.findViewById(R.id.logoutButton);
        profileName = view.findViewById(R.id.profile_name);
        profileEmail = view.findViewById(R.id.profile_email);

        if (user != null) {
            Log.d("ProfileFragment", user.toString());
            // User logged in
            profileName.setText(user.getFullName());
            profileEmail.setText(user.getEmail());
            profileAreaLayout.setVisibility(View.VISIBLE);
            buttonLogout.setVisibility(View.VISIBLE);
            buttonLogin.setVisibility(View.GONE);
        } else {
            // User not logged in
            profileAreaLayout.setVisibility(View.GONE);
            buttonLogout.setVisibility(View.GONE);
            buttonLogin.setVisibility(View.VISIBLE);
        }

        buttonLogin.setOnClickListener(v -> login());
        buttonLogout.setOnClickListener(v -> logout());
        return view;
    }

    private void login() {
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
    }

    private void logout() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("user");
        editor.apply();
        Log.d("LogOut", "Oke");

        Intent intent = new Intent(requireActivity(), MainActivity.class);
        // Clear the back stack, so the user cannot navigate back to the logout fragment
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        // Start the main activity
        startActivity(intent);
//        // Finish the current activity (logout fragment)
//        requireActivity().finish();
    }

}
