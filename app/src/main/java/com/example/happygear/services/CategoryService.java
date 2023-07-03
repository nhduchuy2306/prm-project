package com.example.happygear.services;

import com.example.happygear.models.Category;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public interface CategoryService {
    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd")
            .create();

    String BASE_URL = "https://my-happygear.azurewebsites.net/happygear/api/";

    CategoryService categoryService = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(CategoryService.class);

    @GET("categories")
    Call<List<Category>> getCategories();
}
