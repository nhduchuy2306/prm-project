package com.example.happygear.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.happygear.MainActivity;
import com.example.happygear.R;
import com.example.happygear.dto.GoogleAuthRequest;
import com.example.happygear.dto.LoginRequest;
import com.example.happygear.models.User;
import com.example.happygear.services.UserService;
import com.example.happygear.utils.SerializableObject;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextUsername;
    private EditText editTextPassword;
    private Button buttonLogin;
    private ImageView googleSignIn;

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextUsername = findViewById(R.id.username);
        editTextPassword = findViewById(R.id.password);
        buttonLogin = findViewById(R.id.button_signin);
        googleSignIn = findViewById(R.id.google_signin);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        gsc = GoogleSignIn.getClient(this, gso);

        googleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        // Call api check login with username and password
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUsername.getText().toString();
                String password = editTextPassword.getText().toString();

                // Call api check information login
                checkLogin(username, password);
            }
        });
    }

    private void checkLogin(String username, String password) {
        Call<User> call = UserService.userService.login(new LoginRequest(username, password));
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User user = response.body();
                    try {
                        String userSerialized = SerializableObject.serializeObject(user);
                        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("user", userSerialized);
                        editor.apply();

                        // Redirect to Main Activity
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } catch (Exception e) {
                        Log.e("LoginActivity", e.getMessage());
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Wrong username or password", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Login fail", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void signIn() {
        Intent signInIntent = gsc.getSignInIntent();
        startActivityForResult(signInIntent, 1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1000) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                task.getResult(ApiException.class);
                navigateToMainActivity();
            } catch (ApiException e) {
                Log.e("LoginActivity", e.getMessage());
            }
        }
    }

    private void navigateToMainActivity() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        GoogleAuthRequest googleAuthRequest = new GoogleAuthRequest(
                account.getDisplayName(),
                account.getEmail(),
                "",
                "",
                "",
                account.getId()
        );
        registerWithGoogle(googleAuthRequest);

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        finish();
        startActivity(intent);
    }

    private void registerWithGoogle(GoogleAuthRequest googleAuthRequest) {
        UserService.userService.registerWithGoogle(googleAuthRequest).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Toast.makeText(getApplicationContext(), "Login success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Register fail", Toast.LENGTH_SHORT).show();
            }
        });
    }
}