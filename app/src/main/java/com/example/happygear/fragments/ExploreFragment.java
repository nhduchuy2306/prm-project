package com.example.happygear.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.happygear.R;
import com.example.happygear.activities.ProductDetailActivity;
import com.example.happygear.adapters.ProducPaginationtAdapter;
import com.example.happygear.databases.AppDatabase;
import com.example.happygear.dto.CartDto;
import com.example.happygear.interfaces.ProductCardItemListener;
import com.example.happygear.models.Product;
import com.example.happygear.models.ProductModel;
import com.example.happygear.services.ProductService;
import com.example.happygear.utils.PaginationScrollListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExploreFragment extends Fragment implements ProductCardItemListener {
    private RecyclerView recyclerView;
    private ProducPaginationtAdapter productAdapter;
    private List<Product> mProductList;
    private List<Integer> listCategoryIds = new ArrayList<>();
    private List<Integer> listBrandIds = new ArrayList<>();
    private Double minPrice;
    private Double maxPrice;
    private ImageButton filterButton;
    BottomSheetDialog bottomSheetDialog;
    private RadioButton cbLaptop;
    private RadioButton cbMonitor;
    private RadioButton cbKeyboard;
    private RadioButton cbMouse;
    private RadioButton cbHeadphone;
    private RadioButton cbAcer;
    private RadioButton cbAsus;
    private RadioButton cbDell;
    private RadioButton cbHp;
    private RadioButton cbLogitech;
    private RadioButton cbCorsair;
    private EditText etMinPrice;
    private EditText etMaxPrice;
    private String search;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int currentPage = 1;
    private int totalPage = 1;
    private AppDatabase db;
    private EditText etSearch;
    private ImageView btnSearch;
    private ProgressBar progressBar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = Room.databaseBuilder(requireContext(), AppDatabase.class, "cart.db").allowMainThreadQueries().build();
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explore, container, false);

        filterButton = view.findViewById(R.id.btnFilter);
        etSearch = view.findViewById(R.id.explore_search);
        btnSearch = view.findViewById(R.id.explore_btn_search);
        progressBar = view.findViewById(R.id.explore_progress_bar);

        btnSearch.setOnClickListener(v -> {
            search = etSearch.getText().toString();
            setFirstData();
        });

        recyclerView = view.findViewById(R.id.explore_recycler_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        mProductList = new ArrayList<>();
        productAdapter = new ProducPaginationtAdapter(this);

        recyclerView.setAdapter(productAdapter);
        setListIds();
        setFirstData();
        setFilterData();
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

    private void setListIds() {
        listCategoryIds.add(1);
        listCategoryIds.add(2);
        listCategoryIds.add(3);
        listCategoryIds.add(4);
        listCategoryIds.add(5);
        listBrandIds.add(1);
        listBrandIds.add(2);
        listBrandIds.add(3);
        listBrandIds.add(4);
        listBrandIds.add(6);
    }

    private void setFilterData() {
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View dialogView = getLayoutInflater().inflate(R.layout.bottom_filter, null);

                cbLaptop = dialogView.findViewById(R.id.cbLaptop);
                cbMonitor = dialogView.findViewById(R.id.cbMonitor);
                cbKeyboard = dialogView.findViewById(R.id.cbKeyboard);
                cbMouse = dialogView.findViewById(R.id.cbMouse);
                cbHeadphone = dialogView.findViewById(R.id.cbHeadphone);
                cbAcer = dialogView.findViewById(R.id.cbAcer);
                cbAsus = dialogView.findViewById(R.id.cbAsus);
                cbDell = dialogView.findViewById(R.id.cbDell);
                cbHp = dialogView.findViewById(R.id.cbHP);
                cbLogitech = dialogView.findViewById(R.id.cbLogitech);
                cbCorsair = dialogView.findViewById(R.id.cbCorsair);
                etMinPrice = dialogView.findViewById(R.id.etMinPrice);
                etMaxPrice = dialogView.findViewById(R.id.etMaxPrice);
                setRadioEvent(cbLaptop);
                setRadioEvent(cbMonitor);
                setRadioEvent(cbKeyboard);
                setRadioEvent(cbMouse);
                setRadioEvent(cbHeadphone);
                setRadioEvent(cbAcer);
                setRadioEvent(cbAsus);
                setRadioEvent(cbDell);
                setRadioEvent(cbHp);
                setRadioEvent(cbLogitech);
                setRadioEvent(cbCorsair);

                etMinPrice.setOnEditorActionListener((v1, actionId, event) -> {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        minPrice = Double.parseDouble(etMinPrice.getText().toString());
                        //Toast.makeText(getContext(), minPrice.toString(), Toast.LENGTH_SHORT).show();
                        return true;
                    }
                    return false;
                });

                etMaxPrice.setOnEditorActionListener((v1, actionId, event) -> {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        maxPrice = Double.parseDouble(etMaxPrice.getText().toString());
                        //Toast.makeText(getContext(), etMaxPrice.toString(), Toast.LENGTH_SHORT).show();
                        return true;
                    }
                    return false;
                });


                bottomSheetDialog = new BottomSheetDialog(getContext());
                bottomSheetDialog.setOnShowListener(dialog -> {
                    minPrice = null;
                    maxPrice = null;
                    search = null;
                    listCategoryIds.clear();
                    listBrandIds.clear();
                    currentPage = 1;
                    totalPage = 1;
                });

                bottomSheetDialog.setOnCancelListener(dialog -> {
                    productAdapter.setProductList(null);
                    setFirstData();
                });
                bottomSheetDialog.setContentView(dialogView);
                bottomSheetDialog.show();


            }
        });
    }

    private void setRadioEvent(RadioButton radioButton) {
        radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!radioButton.isSelected()) {
                    radioButton.setChecked(true);
                    radioButton.setSelected(true);
                    switch (radioButton.getText().toString()) {
                        case "Laptop":
                            listCategoryIds.add(1);
                            break;
                        case "Monitor":
                            listCategoryIds.add(2);
                            break;
                        case "Head phone":
                            listCategoryIds.add(3);
                            break;
                        case "Mouse":
                            listCategoryIds.add(4);
                            break;
                        case "Keyboard":
                            listCategoryIds.add(5);
                            break;
                        case "Acer":
                            listBrandIds.add(1);
                            break;
                        case "Asus":
                            listBrandIds.add(2);
                            break;
                        case "HP":
                            listBrandIds.add(3);
                            break;
                        case "Dell":
                            listBrandIds.add(4);
                            break;
                        case "Logitech":
                            listBrandIds.add(5);
                            break;
                        case "Corsair":
                            listBrandIds.add(6);
                            break;

                    }
                } else {
                    radioButton.setChecked(false);
                    radioButton.setSelected(false);
                    switch (radioButton.getText().toString()) {
                        case "Laptop":
                            listCategoryIds.remove(listCategoryIds.indexOf(1));
                            break;
                        case "Monitor":
                            listCategoryIds.remove(listCategoryIds.indexOf(2));
                            break;
                        case "Head phone":
                            listCategoryIds.remove(listCategoryIds.indexOf(3));
                            break;
                        case "Mouse":
                            listCategoryIds.remove(listCategoryIds.indexOf(4));
                            break;
                        case "Keyboard":
                            listCategoryIds.remove(listCategoryIds.indexOf(5));
                            break;
                        case "Acer":
                            listBrandIds.remove(listBrandIds.indexOf(1));
                            break;
                        case "Asus":
                            listBrandIds.remove(listBrandIds.indexOf(2));
                            break;
                        case "HP":
                            listBrandIds.remove(listBrandIds.indexOf(3));
                            break;
                        case "Dell":
                            listBrandIds.remove(listBrandIds.indexOf(4));
                            break;
                        case "Logitech":
                            listBrandIds.remove(listBrandIds.indexOf(5));
                            break;
                        case "Corsair":
                            listBrandIds.remove(listBrandIds.indexOf(6));
                            break;

                    }
                }
            }

        });
    }

    private void setFirstData() {

        ProductService.productService
                .getProductsFilter(1, 8, listCategoryIds, listBrandIds, minPrice, maxPrice, search)
                .enqueue(new Callback<ProductModel>() {
                    @Override
                    public void onResponse(Call<ProductModel> call, Response<ProductModel> response) {
                        if (response.isSuccessful()) {
                            ProductModel productModel = response.body();
                            if (productModel != null) {
                                mProductList = productModel.getData();
                                totalPage = productModel.getSize();
                                productAdapter.setProductList(mProductList);
                                if (currentPage < totalPage) {
                                    productAdapter.addLoadingFooter();
                                } else {
                                    isLastPage = true;
                                }
                            }

                            if (!etSearch.getText().toString().isEmpty()) {
                                productAdapter.removeLoadingFooter();
                            }
                            progressBar.setVisibility(View.GONE);

                            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);
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
                ProductService.productService
                        .getProductsFilter(currentPage, 8, listCategoryIds, listBrandIds, minPrice, maxPrice, search)
                        .enqueue(new Callback<ProductModel>() {
                            @SuppressLint("NotifyDataSetChanged")
                            @Override
                            public void onResponse(Call<ProductModel> call, Response<ProductModel> response) {
                                if (response.isSuccessful()) {
                                    ProductModel productModel = response.body();
                                    if (productModel != null) {
                                        mProductList.addAll(productModel.getData());
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

    @Override
    public void onCardClick(Product product) {
        Intent intent = new Intent(getContext(), ProductDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("product", product);
        intent.putExtras(bundle);
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
    }
}
