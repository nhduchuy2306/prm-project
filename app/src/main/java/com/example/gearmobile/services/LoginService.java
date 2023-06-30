package com.example.gearmobile.services;

import com.example.gearmobile.dto.LoginRequest;
import com.example.gearmobile.models.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.POST;

public interface LoginService {
    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd")
            .create();

    String BASE_URL = "https://my-happygear.azurewebsites.net/happygear/api/";

    LoginService loginService = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(LoginService.class);

    @POST("users/login")
    Call<User> login(@Body LoginRequest loginRequest);
}
