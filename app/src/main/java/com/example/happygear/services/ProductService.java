package com.example.happygear.services;

import com.example.happygear.models.Product;
import com.example.happygear.models.ProductModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ProductService {
    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd")
            .create();

    String BASE_URL = "https://my-happygear.azurewebsites.net/happygear/api/";

    ProductService productService = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ProductService.class);

    @GET("products")
    Call<ProductModel> getProducts(@Query("page") int page, @Query("limit") int limit);
    @GET("products/latest")
    Call<List<Product>> getLatestProducts();
    @GET("products/best-selling")
    Call<List<Product>> getBestSellingProducts();

    @GET("products")
    Call<ProductModel> getProductsFilter(@Query("page") int page, @Query("limit") int limit,
                                         @Query("categoryIds") List<Integer> categoryIds,
                                         @Query("brandIds") List<Integer> brandIds,
                                         @Query("fromPrice") Double fromPrice,
                                         @Query("toPrice") Double toPrice,
                                         @Query("search") String search);


}
