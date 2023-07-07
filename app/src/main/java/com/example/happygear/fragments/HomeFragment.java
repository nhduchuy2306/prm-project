package com.example.happygear.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.happygear.R;
import com.example.happygear.activities.ProductDetailActivity;
import com.example.happygear.adapters.CategoryAdapter;
import com.example.happygear.adapters.ProductAdapter;
import com.example.happygear.databases.AppDatabase;
import com.example.happygear.dto.CartDto;
import com.example.happygear.interfaces.ProductCardItemListener;
import com.example.happygear.models.Category;
import com.example.happygear.models.Product;
import com.example.happygear.services.CategoryService;
import com.example.happygear.services.ProductService;
import com.example.happygear.utils.AppUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements ProductCardItemListener {

    private RecyclerView latestProductsRecyclerView;
    private RecyclerView bestSellingRecyclerView;
    private RecyclerView categoriesRecyclerView;
    private ProductAdapter latestProductsAdapter;
    private ProductAdapter bestSellingAdapter;
    private CategoryAdapter categoryAdapter;
    private List<Product> mLatestProducts;
    private List<Product> mBestSellingProducts;
    private List<Category> mCategories;
    private TextView tvLatestProducts;
    private TextView tvBestSellingProducts;
    private TextView tvCategories;
    private ProgressBar progressBar;
    private ImageSlider imageSlider;
    private AppDatabase db;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = Room.databaseBuilder(requireContext(), AppDatabase.class, "cart.db").allowMainThreadQueries().build();

        if (!AppUtil.isNetworkAvailable(requireContext())) {
            Toast.makeText(requireContext(), "Network disconnected", Toast.LENGTH_SHORT).show();
        }
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        tvCategories = view.findViewById(R.id.home_category_text);
        tvCategories.setVisibility(View.GONE);
        tvBestSellingProducts = view.findViewById(R.id.home_best_selling_text);
        tvBestSellingProducts.setVisibility(View.GONE);
        tvLatestProducts = view.findViewById(R.id.home_latest_text);
        tvLatestProducts.setVisibility(View.GONE);
        progressBar = view.findViewById(R.id.home_progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        imageSlider = view.findViewById(R.id.image_home_slider);

        imageSlider.setImageList(new ArrayList<>(Arrays.asList(
                new SlideModel(R.drawable.banner_1, ScaleTypes.FIT),
                new SlideModel(R.drawable.banner_2, ScaleTypes.FIT),
                new SlideModel(R.drawable.banner_3, ScaleTypes.FIT),
                new SlideModel(R.drawable.banner_4, ScaleTypes.FIT),
                new SlideModel(R.drawable.banner_5, ScaleTypes.FIT)
        )));

        // Latest Products
        latestProductsRecyclerView = view.findViewById(R.id.home_latest_recycler_view);
        LinearLayoutManager latestProductLinearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        latestProductsRecyclerView.setLayoutManager(latestProductLinearLayoutManager);
        latestProductsAdapter = new ProductAdapter(this);
        mLatestProducts = new ArrayList<>();
        latestProductsRecyclerView.setAdapter(latestProductsAdapter);
        loadLatestProducts();

        // Best Selling Products
        bestSellingRecyclerView = view.findViewById(R.id.home_best_selling_recycler_view);
        LinearLayoutManager bestSellingLinearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        bestSellingRecyclerView.setLayoutManager(bestSellingLinearLayoutManager);
        bestSellingAdapter = new ProductAdapter(this);
        mBestSellingProducts = new ArrayList<>();
        bestSellingRecyclerView.setAdapter(bestSellingAdapter);
        loadBestSellingProducts();

        // Categories
        categoriesRecyclerView = view.findViewById(R.id.home_category_recycler_view);
        LinearLayoutManager categoryLinearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        categoriesRecyclerView.setLayoutManager(categoryLinearLayoutManager);
        categoryAdapter = new CategoryAdapter(getContext());
        mCategories = new ArrayList<>();
        categoriesRecyclerView.setAdapter(categoryAdapter);
        loadCategory();

        return view;
    }

    private void loadLatestProducts() {
        ProductService.productService.getLatestProducts().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful()) {
                    List<Product> latestProducts = response.body();
                    if (latestProducts != null) {
                        mLatestProducts = latestProducts;
                        latestProductsAdapter.setProductList(mLatestProducts);
                        latestProductsAdapter.notifyDataSetChanged();

                        tvLatestProducts.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Log.e("HomeFragment", "onFailure: " + t.getMessage());
            }
        });
    }

    private void loadBestSellingProducts() {
        ProductService.productService.getBestSellingProducts().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful()) {
                    List<Product> bestSellingProducts = response.body();
                    if (bestSellingProducts != null) {
                        mBestSellingProducts = bestSellingProducts;
                        bestSellingAdapter.setProductList(mBestSellingProducts);
                        bestSellingAdapter.notifyDataSetChanged();

                        tvBestSellingProducts.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Log.e("HomeFragment", "onFailure: " + t.getMessage());
            }
        });
    }

    private void loadCategory() {
        CategoryService.categoryService.getCategories().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful()) {
                    List<Category> categories = response.body();
                    if (categories != null) {
                        mCategories = categories;
                        categoryAdapter.setCategoryList(mCategories);
                        categoryAdapter.notifyDataSetChanged();

                        tvCategories.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Log.e("HomeFragment", "onFailure: " + t.getMessage());
            }
        });
    }

    @Override
    public void onCardClick(Product product) {
        Intent intent = new Intent(getContext(), ProductDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("product", product);
        intent.putExtra("product", product);
        startActivity(intent);
    }

    @Override
    public void addToCart(Product product) {
        CartDto existCart = db.cartDao().getCartItem(product.getProductId());
        if (existCart != null) {
            existCart.setQuantity(existCart.getQuantity() + 1);
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

        Toast.makeText(getContext(), "Added to cart", Toast.LENGTH_SHORT).show();
    }
}
