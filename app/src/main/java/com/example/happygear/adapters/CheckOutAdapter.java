package com.example.happygear.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.happygear.R;
import com.example.happygear.dto.CheckoutDto;

import java.util.List;

public class CheckOutAdapter extends RecyclerView.Adapter<CheckOutAdapter.CheckOutViewHolder> {

    private List<CheckoutDto> checkoutDtoList;

    @SuppressLint("NotifyDataSetChanged")
    public void setDataCheckout(List<CheckoutDto> checkoutDtoList) {
        this.checkoutDtoList = checkoutDtoList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CheckOutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart_checkout, parent, false);
        return new CheckOutViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CheckOutViewHolder holder, int position) {
        CheckoutDto checkoutDto = checkoutDtoList.get(position);
        if (checkoutDto == null) {
            return;
        }
        String formattedNumber = String.valueOf(checkoutDto.getPrice()).replaceAll("\\.0+$", "");

        holder.tvName.setText(checkoutDto.getName());
        holder.tvPrice.setText("$"+formattedNumber);
        holder.tvQuantity.setText(String.valueOf(checkoutDto.getQuantity()));
    }

    @Override
    public int getItemCount() {
        if (checkoutDtoList != null)
            return checkoutDtoList.size();
        return 0;
    }

    public class CheckOutViewHolder extends RecyclerView.ViewHolder {

        private TextView tvName, tvPrice, tvQuantity;

        public CheckOutViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tv_item_cart_checkout_name);
            tvPrice = itemView.findViewById(R.id.tv_item_cart_checkout_price);
            tvQuantity = itemView.findViewById(R.id.tv_item_cart_checkout_quantity);
        }
    }
}
