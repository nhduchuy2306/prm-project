package com.example.happygear.services;

import com.example.happygear.dto.CartDto;
import com.example.happygear.dto.CreateOrderDto;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface OrderService {
    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd")
            .setLenient()
            .create();

    String BASE_URL = "https://my-happygear.azurewebsites.net/happygear/api/";

    OrderService orderService = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(OrderService.class);

    @POST("orders")
    Call<String> createOrder(@Body CreateOrderDto createOrderDto);
}
