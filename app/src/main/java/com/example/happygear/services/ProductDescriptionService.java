package com.example.happygear.services;

import com.example.happygear.models.ProductDescription;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ProductDescriptionService {
    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd")
            .setLenient()
            .create();

    String BASE_URL = "https://my-happygear.azurewebsites.net/happygear/api/";

    ProductDescriptionService productDescriptionService = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ProductDescriptionService.class);
    @GET("products/{productId}/description")
    Call<ProductDescription> getProductDescription(@Path("productId") int productId);
}
