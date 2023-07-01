package com.example.gearmobile.activities;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.gearmobile.R;
import com.example.gearmobile.models.Product;
import com.example.gearmobile.models.ProductPicture;
import com.example.gearmobile.services.ProductPictureService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailActivity extends AppCompatActivity {

    private Product product;
    private List<SlideModel> imageList;
    private ImageSlider imageSlider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        Bundle bundle = getIntent().getExtras();
        product = (Product) bundle.getSerializable("product");
        imageSlider = findViewById(R.id.image_slider);
        imageList = new ArrayList<>();
        loadProductPicture();
    }

    private void loadProductPicture() {
        ProductPictureService.productService.getProductPictures(product.getProductId()).enqueue(
            new Callback<List<ProductPicture>>() {
                @Override
                public void onResponse(Call<List<ProductPicture>> call, Response<List<ProductPicture>> response) {
                    if (response.isSuccessful()) {
                        List<ProductPicture> productPictures = response.body();
                        for (int i = 0; i < productPictures.size(); i++) {
                            imageList.add(new SlideModel(
                                    productPictures.get(i).getPictureUrl(),
                                    "Product " + i+ 1 + " image",
                                    ScaleTypes.FIT));
                        }
                        imageSlider.setImageList(imageList);
                    }
                }

                @Override
                public void onFailure(retrofit2.Call<List<com.example.gearmobile.models.ProductPicture>> call, Throwable t) {
                    Log.d("ProductDetailActivity", "onFailure: " + t.getMessage());
                }
            }
        );
    }
}