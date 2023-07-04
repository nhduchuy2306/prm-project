package com.example.happygear.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.happygear.R;
import com.example.happygear.interfaces.ProductCardItemListener;
import com.example.happygear.models.Product;
import com.example.happygear.models.ProductDescription;
import com.example.happygear.models.ProductPicture;
import com.example.happygear.services.ProductDescriptionService;
import com.example.happygear.services.ProductPictureService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailActivity extends AppCompatActivity {

    private Product product;
    private List<SlideModel> imageList;
    private ImageSlider imageSlider;
    // Lấy tham chiếu đến các nút
    private ImageButton decreaseButton ;
    private ImageButton increaseButton ;
    private int quantity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        Bundle bundle = getIntent().getExtras();
        product = (Product) bundle.getSerializable("product");
        imageSlider = findViewById(R.id.image_slider);
        imageList = new ArrayList<>();
        //Back button
        Toolbar toolbar = findViewById(R.id.toolbar_product_detail_activity);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //Xử lý tăng giảm số lượn
        // Gán xử lý sự kiện cho nút giảm
        decreaseButton= findViewById(R.id.decreaseButton);
        decreaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Xử lý sự kiện khi nút giảm được nhấn
                // Ví dụ: Giảm giá trị của TextView quantityTextView
                TextView quantityTextView = findViewById(R.id.quantityTextView);
                int quantity = Integer.parseInt(quantityTextView.getText().toString());
                if (quantity > 0) {
                    quantity--;
                    quantityTextView.setText(String.valueOf(quantity));
                }
            }
        });

        // Gán xử lý sự kiện cho nút tăng
        increaseButton = findViewById(R.id.increaseButton);
        increaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Xử lý sự kiện khi nút tăng được nhấn
                // Ví dụ: Tăng giá trị của TextView quantityTextView
                TextView quantityTextView = findViewById(R.id.quantityTextView);
                quantity = Integer.parseInt(quantityTextView.getText().toString());
                quantity++;
                quantityTextView.setText(String.valueOf(quantity));
            }
        });


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
                public void onFailure(retrofit2.Call<List<com.example.happygear.models.ProductPicture>> call, Throwable t) {
                    Log.d("ProductDetailActivity", "onFailure: " + t.getMessage());
                }
            }
        );
    }




}