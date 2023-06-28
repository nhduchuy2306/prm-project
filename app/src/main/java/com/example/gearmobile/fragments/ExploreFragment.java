package com.example.gearmobile.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gearmobile.R;
import com.example.gearmobile.adapters.ProductAdapter;
import com.example.gearmobile.interfaces.ICardItemClick;
import com.example.gearmobile.models.Product;
import com.example.gearmobile.models.ProductModel;
import com.example.gearmobile.services.ProductService;
import com.example.gearmobile.utils.PaginationScrollListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExploreFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<Product> mProductList;

    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int currentPage = 1;
    private int totalPage = 1;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explore, container, false);

        Toolbar toolbar = view.findViewById(R.id.explore_toolbar);
        toolbar.setTitle("Explore");

        recyclerView = view.findViewById(R.id.explore_recycler_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        mProductList = new ArrayList<>();
        setFirstData();
        recyclerView.addOnScrollListener(new PaginationScrollListener(gridLayoutManager) {
            @Override
            public void loadMoreItems() {
                isLoading = true;
                currentPage += 1;
                loadNextPage();
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }
        });

        return view;
    }

    private void setFirstData() {
        ProductService.productService.getProducts(1, 8).enqueue(new Callback<ProductModel>() {
            @Override
            public void onResponse(Call<ProductModel> call, Response<ProductModel> response) {
                if (response.isSuccessful()) {
                    ProductModel productModel = response.body();
                    if (productModel != null) {
                        mProductList = productModel.getData();
                        totalPage = productModel.getSize();
                        productAdapter = new ProductAdapter(new ICardItemClick() {
                            @Override
                            public void onCardClick(Product product) {
                                Log.d("ExploreFragment", "onCardClick: " + product);
                            }

                            @Override
                            public void addToCart(Product product) {
                                Log.d("ExploreFragment", "addToCart: " + product);
                            }
                        });
                        productAdapter.setProductList(mProductList);
                        if (currentPage < totalPage) {
                            productAdapter.addLoadingFooter();
                        } else {
                            isLastPage = true;
                        }
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

    private void loadNextPage() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                productAdapter.removeLoadingFooter();
                ProductService.productService.getProducts(currentPage, 8).enqueue(new Callback<ProductModel>() {
                    @Override
                    public void onResponse(Call<ProductModel> call, Response<ProductModel> response) {
                        if (response.isSuccessful()) {
                            ProductModel productModel = response.body();
                            if (productModel != null) {
                                mProductList = productModel.getData();
                                totalPage = productModel.getSize();
                                productAdapter.removeLoadingFooter();
                                isLoading = false;
                                productAdapter.setProductList(mProductList);
                                productAdapter.notifyDataSetChanged();

                                isLoading = false;
                                if (currentPage < totalPage) {
                                    productAdapter.addLoadingFooter();
                                } else {
                                    isLastPage = true;
                                }
                            }
                            recyclerView.setAdapter(productAdapter);
                        }
                    }

                    @Override
                    public void onFailure(Call<ProductModel> call, Throwable t) {
                        Log.e("ExploreFragment", "onFailure: " + t.getMessage());
                    }
                });
            }
        }, 2000);
    }
}
