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

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressViewHolder>{

    private List<ShopAddress> mShopAddresses;
    private AddressListener mAddressListener;

    public AddressAdapter(AddressListener mAddressListener) {
        this.mAddressListener = mAddressListener;
    }

    public void setData(List<ShopAddress> mShopAddresses){
        this.mShopAddresses = mShopAddresses;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_address, parent, false);
        return new AddressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressViewHolder holder, int position) {
        ShopAddress shopAddress = mShopAddresses.get(position);
        if(shopAddress == null){
            return;
        }
        holder.tvAddress.setText(shopAddress.getAddress());
        holder.layoutAddress.setOnClickListener(v -> mAddressListener.onAddressClick(shopAddress));
    }

    @Override
    public int getItemCount() {
        if(mShopAddresses != null){
            return mShopAddresses.size();
        }
        return 0;
    }

    public class AddressViewHolder extends RecyclerView.ViewHolder{

        private LinearLayout layoutAddress;
        private TextView tvAddress;

        public AddressViewHolder(@NonNull View itemView) {
            super(itemView);

            layoutAddress = itemView.findViewById(R.id.layout_address);
            tvAddress = itemView.findViewById(R.id.address_shop);
        }
    }
}
