package com.example.gearmobile.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gearmobile.R;
import com.example.gearmobile.activities.ProductDetailActivity;
import com.example.gearmobile.adapters.CategoryAdapter;
import com.example.gearmobile.adapters.ProductAdapter;
import com.example.gearmobile.interfaces.ProductCardItemListener;
import com.example.gearmobile.models.Category;
import com.example.gearmobile.models.Product;
import com.example.gearmobile.services.CategoryService;
import com.example.gearmobile.services.ProductService;

import java.util.ArrayList;
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

    private EditText etSearch;

    private ImageButton btnSearch;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        Toolbar toolbar = view.findViewById(R.id.home_toolbar);
        etSearch = view.findViewById(R.id.home_search);
        btnSearch = view.findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String search = etSearch.getText().toString();
                if(search.isEmpty()) {
                    Toast.makeText(getContext(), "Please enter a search term", Toast.LENGTH_SHORT).show();
                }
                else {
                    Bundle bundle = new Bundle();
                    bundle.putString("search", search);
                    Fragment fragment = new ExploreFragment();
                    fragment.setArguments(bundle);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.nav_main_fragment, fragment).commit();
                }
            }
        });
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

    }


}
