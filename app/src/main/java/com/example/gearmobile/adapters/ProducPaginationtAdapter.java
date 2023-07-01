package com.example.gearmobile.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gearmobile.DetailProductActivity;
import com.example.gearmobile.R;
import com.example.gearmobile.interfaces.IProductCardItemClick;
import com.example.gearmobile.models.Product;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProducPaginationtAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_PRODUCT = 1;
    private static final int TYPE_LOADING = 2;
    private List<Product> mProductList;
    private boolean isLoadingAdded = false;
    private Context mContext;

    public ProducPaginationtAdapter(Context context) {
        mContext = context;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setProductList(List<Product> productList) {
        mProductList = productList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (mProductList != null && position == mProductList.size() - 1 && isLoadingAdded)
            return TYPE_LOADING;
        return TYPE_PRODUCT;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (TYPE_LOADING == viewType) {
            View loadingView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new ProductViewHolder(loadingView);
        } else {
            View productView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
            return new ProductViewHolder(productView);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder.getItemViewType() == TYPE_LOADING)
            return;
        if (holder instanceof ProductViewHolder) {
            ProductViewHolder productViewHolder = (ProductViewHolder) holder;
            Product product = mProductList.get(position);
            if (product == null)
                return;

            String formattedNumber = String.valueOf(product.getPrice()).replaceAll("\\.0+$", "");

            Picasso.get().load(product.getPicture()).placeholder(androidx.recyclerview.selection.R.drawable.selection_band_overlay).into(productViewHolder.productImage);
            productViewHolder.productName.setText(product.getProductName());
            productViewHolder.productPrice.setText("$"+formattedNumber);
            productViewHolder.productLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("Product", "onClick: " + product.getProductName());
                    mContext = v.getContext();
                    Gson gson = new Gson();
                    String productJson = gson.toJson(product);
                    Intent intent = new Intent(mContext, DetailProductActivity.class);
                    intent.putExtra("product", productJson);
                    mContext.startActivity(intent);
                }
            });
            productViewHolder.productAddToCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("Product", "onClick: " + product.getProductName());
                }
            });
        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        if (mProductList != null)
            return mProductList.size();
        return 0;
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout productLayout;
        public ImageView productImage;
        public TextView productName;
        public TextView productPrice;
        public Button productAddToCart;
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productLayout = itemView.findViewById(R.id.product_item_layout);
            productImage = itemView.findViewById(R.id.product_item_image);
            productName = itemView.findViewById(R.id.product_item_name);
            productPrice = itemView.findViewById(R.id.product_item_price);
            productAddToCart = itemView.findViewById(R.id.product_item_add_to_cart);
        }
    }

    public static class LoadingViewHolder extends RecyclerView.ViewHolder {
        private final ProgressBar progressBar;
        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.loading_progress_bar);
        }
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
        mProductList.add(new Product());
        notifyItemInserted(mProductList.size() - 1);
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;
        int position = mProductList.size() - 1;
        Product product = mProductList.get(position);
        if (product != null) {
            mProductList.remove(position);
            notifyItemRemoved(position);
        }
    }
}
