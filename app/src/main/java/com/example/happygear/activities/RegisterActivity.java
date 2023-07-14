package com.example.happygear.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.happygear.MainActivity;
import com.example.happygear.R;
import com.example.happygear.adapters.CustomArrayAdapter;
import com.example.happygear.dto.GoogleAuthRequest;
import com.example.happygear.dto.RegisterRequest;
import com.example.happygear.models.User;
import com.example.happygear.services.UserService;
import com.example.happygear.models.utils.SerializableObject;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private EditText username;
    private EditText fullName;
    private EditText password;
    private EditText email;
    private EditText phone;
    private EditText address;
    private Spinner genderSn;
    private Boolean gender;
    private Button btnRegister;
    private ImageView ggSignIn;

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;

    boolean isAllFieldsChecked = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbar = findViewById(R.id.register_toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        username = findViewById(R.id.username);
        fullName = findViewById(R.id.fullName);
        password = findViewById(R.id.password);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        genderSn = findViewById(R.id.gender);
        address = findViewById(R.id.address);
        btnRegister = findViewById(R.id.button_register);
        ggSignIn = findViewById(R.id.google_signin);

        // Create an ArrayAdapter using the string array and a default spinner layout
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
//                R.array.gender, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapter = new CustomArrayAdapter(this,
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.gender));
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        genderSn.setAdapter(adapter);


        // Google sign in
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        gsc = GoogleSignIn.getClient(this, gso);

        ggSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        // Handle gender select box
        genderSn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                // If user change the default selection
                // First item is disable and it is used for hint
                if (position > 0) {
                    // Notify the selected item text
//                    Toast.makeText(getApplicationContext(), "Selected : " + selectedItemText, Toast.LENGTH_SHORT).show();
                    if (selectedItemText.equals("Male")) {
                        gender = true;
                    } else if (selectedItemText.equals("Female")) {
                        gender = false;
                    } else {
                        gender = null;
                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                gender = null;
            }

        });

        // Call api register
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAllFieldsChecked = checkAllFields();

                if (isAllFieldsChecked) {
                    RegisterRequest create = new RegisterRequest();
                    create.setUsername(username.getText().toString().trim());
                    create.setFullName(fullName.getText().toString().trim());
                    create.setAddress(address.getText().toString().trim());
                    create.setPassword(password.getText().toString().trim());
                    create.setEmail(email.getText().toString().trim());
                    create.setPhoneNumber(phone.getText().toString().trim());
                    create.setGender(gender);
                    create.setRoleId(0);
                    create.setStatus(true);

                    // Call api
                    createUser(create);
                    gender = null;
                    Toast.makeText(getApplicationContext(), "Welcome " + fullName, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private boolean checkAllFields() {
        if (username.length() == 0) {
            username.setError("User name is required");
            return false;
        }

        if (fullName.length() == 0) {
            fullName.setError("Full name is required");
            return false;
        }

        if (password.length() == 0) {
            password.setError("Password is required");
            return false;
        } else if (password.length() < 8) {
            password.setError("Password must be minimum 8 characters");
            return false;
        }

        if (phone.length() > 0 && phone.length() < 10) {
            phone.setError("Invalid phone number");
            return false;
        }

        if (email.length() > 0) {
            // Define the regular expression pattern for an email address
            String pattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
            if (!email.getText().toString().trim().matches(pattern)){
                email.setError("Invalid email");
                return false;
            }
        }

        // all checked
        return true;
    }

    private void createUser(RegisterRequest registerRequest) {
        Call<User> call = UserService.userService.register(registerRequest);

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
                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } catch (Exception e) {
                        Log.e("RegisterActivity", e.getMessage());
                    }
                } else {
                    Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

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

        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
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
