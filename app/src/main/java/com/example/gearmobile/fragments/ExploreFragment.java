package com.example.gearmobile.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gearmobile.R;
import com.example.gearmobile.adapters.ProductAdapter;
import com.example.gearmobile.interfaces.ICardItemClick;
import com.example.gearmobile.models.Product;
import com.example.gearmobile.models.ProductModel;
import com.example.gearmobile.services.ProductService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExploreFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<Product> mProductList;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explore,container,false);

        Toolbar toolbar = view.findViewById(R.id.explore_toolbar);
        toolbar.setTitle("Explore");

        recyclerView = view.findViewById(R.id.explore_recycler_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        mProductList = new ArrayList<>();
        getProducts();

        return view;
    }

    private void getProducts() {
        ProductService.productService.getProducts(1, 8).enqueue(new Callback<ProductModel>() {
            @Override
            public void onResponse(Call<ProductModel> call, Response<ProductModel> response) {
                if (response.isSuccessful()) {
                    ProductModel productModel = response.body();
                    if (productModel != null) {
                        mProductList = productModel.getData();
                        productAdapter = new ProductAdapter(productModel.getData(), new ICardItemClick() {
                            @Override
                            public void onCardClick(Product product) {
                                Log.d("ExploreFragment", "onCardClick: " + product);

                            }

                            @Override
                            public void addToCart(Product product) {
                                Log.d("ExploreFragment", "addToCart: " + product);
                            }
                        });
                        recyclerView.setAdapter(productAdapter);
                    }
                }
            }
            @Override
            public void onFailure(Call<ProductModel> call, Throwable t) {
                Log.e("ExploreFragment", "onFailure: " + t.getMessage());
            }
        });
    }
}
