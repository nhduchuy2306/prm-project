package com.example.happygear.services;

import com.example.happygear.dto.GoogleAuthRequest;
import com.example.happygear.dto.LoginRequest;
import com.example.happygear.dto.OrderDetailModel;
import com.example.happygear.dto.RegisterRequest;
import com.example.happygear.models.Order;
import com.example.happygear.models.ProductPicture;
import com.example.happygear.models.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

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

    @GET("users/{username}/orders")
    Call<List<Order>> getOrdersbyUsername(@Path("username") String username);

    @GET("users/{username}/order-details")
    Call<List<OrderDetailModel>> getAllOrderDetailsByUsername(@Path("username") String username);
}
