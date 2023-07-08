package com.example.happygear.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.happygear.R;
import com.example.happygear.interfaces.AddressListener;
import com.example.happygear.models.ShopAddress;

import java.util.List;

public class ShopAddressAdapter extends RecyclerView.Adapter<ShopAddressAdapter.ShopAddressViewHolder> {

    private List<ShopAddress> mShopAddressList;
    private AddressListener mAddressListener;

    public ShopAddressAdapter(AddressListener mAddressListener) {
        this.mAddressListener = mAddressListener;
    }

    public void setData(List<ShopAddress> list) {
        this.mShopAddressList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ShopAddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_address, parent, false);
        return new ShopAddressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopAddressViewHolder holder, int position) {
        ShopAddress shopAddress = mShopAddressList.get(position);
        if (shopAddress == null)
            return;
        holder.tvAddress.setText(shopAddress.getAddress());
        holder.addressLayout.setOnClickListener(v -> mAddressListener.onAddressClick(shopAddress));
    }

    @Override
    public int getItemCount() {
        if (mShopAddressList != null)
            return mShopAddressList.size();
        return 0;
    }

    public class ShopAddressViewHolder extends RecyclerView.ViewHolder {
        private TextView tvAddress;
        private LinearLayout addressLayout;

        public ShopAddressViewHolder(@NonNull View itemView) {
            super(itemView);

            tvAddress = itemView.findViewById(R.id.address_shop);
            addressLayout = itemView.findViewById(R.id.layout_address);
        }
    }
}
