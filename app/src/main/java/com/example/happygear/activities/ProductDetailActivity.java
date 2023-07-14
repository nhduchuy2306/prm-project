package com.example.happygear.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.happygear.MainActivity;
import com.example.happygear.R;
import com.example.happygear.adapters.AddressAdapter;
import com.example.happygear.adapters.ProductDescriptionAdapter;
import com.example.happygear.databases.AppDatabase;
import com.example.happygear.dto.CartDto;
import com.example.happygear.dto.DescriptionDto;
import com.example.happygear.interfaces.AddressListener;
import com.example.happygear.models.Product;
import com.example.happygear.models.ProductDescription;
import com.example.happygear.models.ProductPicture;
import com.example.happygear.models.ShopAddress;
import com.example.happygear.services.ProductDescriptionService;
import com.example.happygear.services.ProductPictureService;
import com.example.happygear.services.ProductService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailActivity extends AppCompatActivity implements AddressListener {

    private Product product;
    private List<SlideModel> imageList;
    private ImageSlider imageSlider;
    private ImageView decreaseButton;
    private ImageView increaseButton;
    private TextView quantityTextView;
    private TextView productName;
    private TextView productPrice;
    private Button addToCartButton;
    private RecyclerView recyclerView;
    private RecyclerView addressRecyclerView;
    private List<ShopAddress> shopAddressList;
    private AddressAdapter addressAdapter;
    private List<DescriptionDto> descriptionDtoList;
    private ProductDescription productDescription;
    private ProductDescriptionAdapter productDescriptionAdapter;
    private int quantity;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        Bundle bundle = getIntent().getExtras();
        product = (Product) bundle.getSerializable("product");
        imageSlider = findViewById(R.id.image_slider);
        imageList = new ArrayList<>();

        db = Room.databaseBuilder(this, AppDatabase.class, "cart.db").allowMainThreadQueries().build();

        Toolbar toolbar = findViewById(R.id.toolbar_product_detail_activity);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        loadProductDescription();
        loadProductPicture();

        decreaseButton = findViewById(R.id.detail_btn_decrease);
        increaseButton = findViewById(R.id.detail_btn_increase);
        quantityTextView = findViewById(R.id.detail_product_quantity);
        productName = findViewById(R.id.detail_product_name);
        productPrice = findViewById(R.id.detail_product_price);
        addToCartButton = findViewById(R.id.detail_btn_add_to_cart);
        recyclerView = findViewById(R.id.detail_product_recycler_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ProductDetailActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(ProductDetailActivity.this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        productName.setText(product.getProductName());
        String formattedNumber = String.valueOf(product.getPrice()).replaceAll("\\.0+$", "");
        productPrice.setText("$" + formattedNumber);

        addressRecyclerView = findViewById(R.id.detail_product_address_recycler_view);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(ProductDetailActivity.this, LinearLayoutManager.VERTICAL, false);
        addressRecyclerView.setLayoutManager(linearLayoutManager2);
        addressAdapter = new AddressAdapter(this);
        loadShopAddress();


        decreaseButton.setOnClickListener(v -> setDecreaseButton());
        increaseButton.setOnClickListener(v -> setIncreaseButton());
        addToCartButton.setOnClickListener(v -> setAddToCartButton());
    }

    private List<DescriptionDto> getListDescription(ProductDescription productDescription) {
        List<DescriptionDto> descriptionDtoList = new ArrayList<>();

        descriptionDtoList.add(new DescriptionDto("Key Cap", productDescription.getKeycap()));
        descriptionDtoList.add(new DescriptionDto("Switch KeyBoard", productDescription.getSwitchKeyBoard()));
        descriptionDtoList.add(new DescriptionDto("Type Keyboard", productDescription.getTypeKeyboard()));
        descriptionDtoList.add(new DescriptionDto("Connect", productDescription.getConnect()));
        descriptionDtoList.add(new DescriptionDto("Led", productDescription.getLed()));
        descriptionDtoList.add(new DescriptionDto("Freigh", productDescription.getFreigh()));
        descriptionDtoList.add(new DescriptionDto("Item Dimension", productDescription.getItemDimension()));
        descriptionDtoList.add(new DescriptionDto("CPU", productDescription.getCpu()));
        descriptionDtoList.add(new DescriptionDto("Operating System", productDescription.getOperatingSystem()));
        descriptionDtoList.add(new DescriptionDto("Battery", productDescription.getBattery()));
        descriptionDtoList.add(new DescriptionDto("Hard Disk", productDescription.getHardDisk()));
        descriptionDtoList.add(new DescriptionDto("Graphic Card", productDescription.getGraphicCard()));
        descriptionDtoList.add(new DescriptionDto("KeyBoard", productDescription.getKeyBoard()));
        descriptionDtoList.add(new DescriptionDto("Audio", productDescription.getAudio()));
        descriptionDtoList.add(new DescriptionDto("Wifi", productDescription.getWifi()));
        descriptionDtoList.add(new DescriptionDto("Bluetooth", productDescription.getBluetooth()));
        descriptionDtoList.add(new DescriptionDto("Color", productDescription.getColor()));
        descriptionDtoList.add(new DescriptionDto("Frame Rate", productDescription.getFrameRate()));
        descriptionDtoList.add(new DescriptionDto("Screen Size", productDescription.getScreenSize()));
        descriptionDtoList.add(new DescriptionDto("Screen Type", productDescription.getScreenType()));

        return descriptionDtoList;
    }

    private void setIncreaseButton() {
        quantity = Integer.parseInt(quantityTextView.getText().toString());
        quantity++;
        quantityTextView.setText(String.valueOf(quantity));
    }

    private void setDecreaseButton() {
        int quantity = Integer.parseInt(quantityTextView.getText().toString());
        if (quantity > 1) {
            quantity--;
            quantityTextView.setText(String.valueOf(quantity));
        }
    }

    private void setAddToCartButton() {
        CartDto existCart = db.cartDao().getCartItem(product.getProductId());
        if (existCart != null) {
            existCart.setQuantity(existCart.getQuantity() + Integer.parseInt(quantityTextView.getText().toString()));
            new Thread(() -> {
                db.cartDao().update(existCart.getProductId(), existCart.getQuantity());
            }).start();
            return;
        }
        CartDto cartDto = new CartDto(
                product.getProductId(),
                1,
                product.getPrice(),
                product.getProductName(),
                product.getPicture()
        );

        new Thread(() -> {
            db.cartDao().insert(cartDto);
        }).start();

        Toast.makeText(this, "Added to cart", Toast.LENGTH_SHORT).show();

        new Thread(() -> {
            int cartSize = db.cartDao().getCartCount();
            Log.d("ProductDetailActivity", "setAddToCartButton: " + cartSize);
            updateCartInMainActivity(cartSize);
        }).start();
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
                                        ScaleTypes.FIT));
                            }
                            imageSlider.setImageList(imageList);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<com.example.happygear.models.ProductPicture>> call, Throwable t) {
                        Log.d("ProductDetailActivity", "onFailure: " + t.getMessage());
                    }
                }
        );
    }

    private void loadProductDescription() {
        ProductDescriptionService.productDescriptionService
                .getProductDescription(product.getProductId()).enqueue(new Callback<ProductDescription>() {
                    @Override
                    public void onResponse(Call<ProductDescription> call, Response<ProductDescription> response) {
                        if (response.isSuccessful()) {
                            productDescription = response.body();
                            descriptionDtoList = getListDescription(productDescription);
                            productDescriptionAdapter = new ProductDescriptionAdapter(descriptionDtoList);
                            recyclerView.setAdapter(productDescriptionAdapter);
                        }
                    }

                    @Override
                    public void onFailure(Call<ProductDescription> call, Throwable t) {
                        Log.d("ProductDetailActivity", "onFailure: " + t.getMessage());
                    }
                });
    }

    private void loadShopAddress() {
        ProductService.productService.getProductAddressById(product.getProductId()).enqueue(new Callback<Product>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if (response.isSuccessful()) {
                    shopAddressList = response.body().getShopAddresses();
                    addressAdapter.setData(shopAddressList);
                    addressAdapter.notifyDataSetChanged();
                    addressRecyclerView.setAdapter(addressAdapter);
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                Log.d("ProductDetailActivity", "onFailure: " + t.getMessage());
            }
        });
    }

    @Override
    public void onAddressClick(ShopAddress shopAddress) {
        Intent intent = new Intent(ProductDetailActivity.this, MapsActivity.class);
        intent.putExtra("shopAddress", shopAddress);
        startActivity(intent);
    }

    private void updateCartInMainActivity(int cartSize) {
        Intent intent = new Intent(MainActivity.ACTION_UPDATE_CART_BADGE);
        intent.putExtra("cartSize", cartSize);
        sendBroadcast(intent);
    }
}