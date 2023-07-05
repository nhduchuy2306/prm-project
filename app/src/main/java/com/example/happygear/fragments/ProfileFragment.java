package com.example.happygear.fragments;

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

import com.example.happygear.MainActivity;
import com.example.happygear.R;
import com.example.happygear.activities.LoginActivity;
import com.example.happygear.activities.OrderHistoryActivity;
import com.example.happygear.activities.RegisterActivity;
import com.example.happygear.models.User;
import com.example.happygear.utils.SerializableObject;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class ProfileFragment extends Fragment {

    private LinearLayout profileAreaLayout;

    private LinearLayout profileAreaOrderHistoryLayout;
    private LinearLayout profileAreaAboutUsLayout;
    private LinearLayout profileAreaMapLayout;
    private TextView profileName;
    private TextView profileEmail;

    private TextView profileOrderHistory;
    private TextView profileAboutUs;
    private TextView profileMap;
    private Button buttonLogout;
    private Button buttonLogin;
    private Button buttonRegister;
    private SharedPreferences sharedPreferences;
    private User user;
    private GoogleSignInOptions gso;
    private GoogleSignInClient gsc;
    private GoogleSignInAccount account;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();
            gsc = GoogleSignIn.getClient(requireContext(), gso);

            sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            String userSerialized = sharedPreferences.getString("user", null);
            if (userSerialized != null) {
                user = (User) SerializableObject.deserializeObject(userSerialized);
            }
            account = GoogleSignIn.getLastSignedInAccount(requireContext());
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
        buttonRegister = view.findViewById(R.id.registerButton);
        buttonLogout = view.findViewById(R.id.logoutButton);
        profileName = view.findViewById(R.id.profile_name);
        profileEmail = view.findViewById(R.id.profile_email);
        profileOrderHistory = view.findViewById(R.id.text_myorder);
        profileAreaOrderHistoryLayout = view.findViewById(R.id.myorder_area);
        profileAboutUs = view.findViewById(R.id.text_aboutus);
        profileAreaAboutUsLayout = view.findViewById(R.id.aboutus_area);
        profileMap = view.findViewById(R.id.text_map);
        profileAreaMapLayout = view.findViewById(R.id.map_area);

        if (account != null) {
            String personName = account.getDisplayName();
            String personEmail = account.getEmail();

            profileName.setText(personName);
            profileEmail.setText(personEmail);
            profileAreaLayout.setVisibility(View.VISIBLE);
            profileAreaOrderHistoryLayout.setVisibility(View.VISIBLE);
            profileOrderHistory.setVisibility(View.VISIBLE);
            profileAreaAboutUsLayout.setVisibility(View.VISIBLE);
            profileAboutUs.setVisibility(View.VISIBLE);
            profileAreaMapLayout.setVisibility(View.VISIBLE);
            profileMap.setVisibility(View.VISIBLE);
            buttonLogout.setVisibility(View.VISIBLE);
            buttonLogin.setVisibility(View.GONE);
            buttonRegister.setVisibility(View.GONE);

            profileOrderHistory.setOnClickListener(v -> openOrderHistory(account.getId()));
        } else if (user != null) {
            // User logged in
            profileName.setText(user.getFullName());
            profileEmail.setText(user.getEmail());
            //
            profileAreaLayout.setVisibility(View.VISIBLE);
            profileAreaOrderHistoryLayout.setVisibility(View.VISIBLE);
            profileOrderHistory.setVisibility(View.VISIBLE);
            profileAreaAboutUsLayout.setVisibility(View.VISIBLE);
            profileAboutUs.setVisibility(View.VISIBLE);
            profileAreaMapLayout.setVisibility(View.VISIBLE);
            profileMap.setVisibility(View.VISIBLE);
            //
            buttonLogout.setVisibility(View.VISIBLE);
            buttonLogin.setVisibility(View.GONE);
            buttonRegister.setVisibility(View.GONE);
            profileOrderHistory.setOnClickListener(v -> openOrderHistory(user.getUsername()));

        } else {
            // User not logged in
            profileAreaLayout.setVisibility(View.GONE);
            buttonLogout.setVisibility(View.GONE);
            buttonLogin.setVisibility(View.VISIBLE);
            buttonRegister.setVisibility(View.VISIBLE);
        }

        buttonLogin.setOnClickListener(v -> login());
        buttonRegister.setOnClickListener(v -> register());
        buttonLogout.setOnClickListener(v -> logout());




        return view;
    }

    private void login() {
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
    }

    private void register() {
        Intent intent = new Intent(getContext(), RegisterActivity.class);
        startActivity(intent);
    }

    private void openOrderHistory(String username){
        Intent intent = new Intent(getContext(), OrderHistoryActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }


    private void logout() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("user");
        editor.apply();

        if (account != null) {
            gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Intent intent = new Intent(requireActivity(), MainActivity.class);
                    startActivity(intent);
                }
            });
        } else {
            Intent intent = new Intent(requireActivity(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

}
