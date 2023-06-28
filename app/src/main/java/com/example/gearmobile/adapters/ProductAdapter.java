package com.example.gearmobile.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gearmobile.R;
import com.example.gearmobile.interfaces.ICardItemClick;
import com.example.gearmobile.models.Product;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> mProductList;
    private ICardItemClick mICardItemClick;

    public ProductAdapter(List<Product> productList, ICardItemClick iCardItemClick) {
        mProductList = productList;
        mICardItemClick = iCardItemClick;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View productView = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
        return new ProductViewHolder(productView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = mProductList.get(position);
        if (product == null)
            return;

        String formattedNumber = String.valueOf(product.getPrice()).replaceAll("\\.0+$", "");

        Picasso.get().load(product.getPicture()).placeholder(R.drawable.ic_launcher_background).into(holder.productImage);
        holder.productName.setText(product.getProductName());
        holder.productPrice.setText("$"+formattedNumber);
        holder.productLayout.setOnClickListener(v -> mICardItemClick.onCardClick(product));
        holder.productAddToCart.setOnClickListener(v -> mICardItemClick.addToCart(product));
    }

    @Override
    public int getItemCount() {
        if (mProductList != null)
            return mProductList.size();
        return 0;
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
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
}
