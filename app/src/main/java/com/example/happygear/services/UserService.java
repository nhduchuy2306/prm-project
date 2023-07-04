package com.example.happygear.services;

import com.example.happygear.dto.GoogleAuthRequest;
import com.example.happygear.dto.LoginRequest;
import com.example.happygear.dto.RegisterRequest;
import com.example.happygear.models.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface UserService {
    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd")
            .create();

    String BASE_URL = "https://my-happygear.azurewebsites.net/happygear/api/";

    UserService userService = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(UserService.class);

    @POST("users/login")
    Call<User> login(@Body LoginRequest loginRequest);
    @GET("users")
    Call<User> getUserByUsername(@Field("username") String username);
    @POST("users/google/auth")
    Call<User> registerWithGoogle(@Body GoogleAuthRequest googleAuthRequest);
    @POST("users/register")
    Call<User> register(@Body RegisterRequest registerRequest);
}