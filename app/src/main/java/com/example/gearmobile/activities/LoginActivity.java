package com.example.gearmobile.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gearmobile.MainActivity;
import com.example.gearmobile.R;
import com.example.gearmobile.dto.LoginRequest;
import com.example.gearmobile.fragments.ExploreFragment;
import com.example.gearmobile.models.User;
import com.example.gearmobile.services.LoginService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextUsername;
    private EditText editTextPassword;
    private Button buttonLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editTextUsername = findViewById(R.id.username);
        editTextPassword = findViewById(R.id.password);
        buttonLogin = findViewById(R.id.button_signin);


                // Gọi API để kiểm tra thông tin đăng nhập
                buttonLogin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String username = editTextUsername.getText().toString();
                        String password = editTextPassword.getText().toString();

                        // Gọi API để kiểm tra thông tin đăng nhập
                        checkLogin(username, password);
                    }
                });



    }
    private void checkLogin(String username, String password) {

        Call<User> call = LoginService.loginService.login(new LoginRequest(username,password));
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User user = response.body();

                        // Đăng nhập thành công, chuyển sang Activity mới
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish(); // Tùy chọn: Đóng Activity hiện tại sau khi chuyển sang Activity mới

                } else {
                    Toast.makeText(getApplicationContext(), "Sai username hoặc password", Toast.LENGTH_SHORT).show();                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }

}