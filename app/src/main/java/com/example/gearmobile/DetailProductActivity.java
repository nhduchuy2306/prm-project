package com.example.gearmobile;

import static com.example.gearmobile.services.ProductService.productService;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.example.gearmobile.R;
import com.example.gearmobile.adapters.ImagePagerAdapter;
import com.example.gearmobile.adapters.ProductDescriptionAdapter;
import com.example.gearmobile.models.Product;
import com.example.gearmobile.models.ProductDescription;
import com.example.gearmobile.services.ProductService;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailProductActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private Context context;
    private ProductDescription productDescription;
    private ProductDescriptionAdapter productDescriptionAdapter;
    private Product product;
   // private List<TextView> descriptionTextViews = new ArrayList<>();
    List<Integer> imageList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_product);

        // Thiết lập thanh toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        viewPager = findViewById(R.id.viewPager);
        ImagePagerAdapter adapter = new ImagePagerAdapter(this, imageList);
        viewPager.setAdapter(adapter);

        productDescriptionAdapter= new ProductDescriptionAdapter(context,productDescription);

        //Lấy product từ explore qua
        Intent intent = getIntent();
        Gson gson = new Gson();
        String productJson = intent.getStringExtra("product");
        product = gson.fromJson(productJson, Product.class);

        getProductDescription();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public ProductDescription getProductDescription(){
        Call<ProductDescription> call = ProductService.productService.getProductDescription(product.getProductId());
        call.enqueue(new Callback<ProductDescription>() {
            @Override
            public void onResponse(Call<ProductDescription> call, Response<ProductDescription> response) {
                if (response.isSuccessful()) {
                    productDescription = response.body();
                    // Hiển thị thông tin sản phẩm lên TextViews

                    // Các TextViews khác tương tự

                } else {
                    // Xử lý khi request không thành công
                }
            }

            @Override
            public void onFailure(Call<ProductDescription> call, Throwable t) {
                // Xử lý khi có lỗi xảy ra
            }
        });
        return productDescription;
    }

}
