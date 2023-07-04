package com.example.happygear.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.happygear.R;
import com.example.happygear.dto.CartDto;
import com.example.happygear.interfaces.CartItemListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<CartDto> cartDtoList;
    private final CartItemListener cartItemListener;

    public CartAdapter(CartItemListener cartItemListener) {
        this.cartItemListener = cartItemListener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setCartDtoList(List<CartDto> cartDtoList) {
        this.cartDtoList = cartDtoList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartDto cartDto = cartDtoList.get(position);
        if(cartDto == null) return;

        String formattedNumber = String.valueOf(cartDto.getPrice()).replaceAll("\\.0+$", "");
        Picasso.get().load(cartDto.getProductImage())
                .placeholder(androidx.recyclerview.selection.R.drawable.selection_band_overlay).into(holder.cartImage);

        holder.cartName.setText(cartDto.getProductName());
        holder.cartPrice.setText("$"+formattedNumber);
        holder.cartQuantity.setText(String.valueOf(cartDto.getQuantity()));
        holder.cartIncrease.setOnClickListener(v -> cartItemListener.onIncrease(cartDto));
        holder.cartDecrease.setOnClickListener(v -> cartItemListener.onDecrease(cartDto));
    }

    @Override
    public int getItemCount() {
        if(cartDtoList != null)
            return cartDtoList.size();
        return 0;
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {
        private ImageView cartImage;
        private TextView cartName;
        private TextView cartPrice;
        private Button cartIncrease;
        private TextView cartQuantity;
        private Button cartDecrease;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            cartImage = itemView.findViewById(R.id.cart_image);
            cartName = itemView.findViewById(R.id.cart_name);
            cartPrice = itemView.findViewById(R.id.cart_price);
            cartIncrease = itemView.findViewById(R.id.cart_increase);
            cartQuantity = itemView.findViewById(R.id.cart_quantity);
            cartDecrease = itemView.findViewById(R.id.cart_decrease);
        }
    }
}
