package com.example.happygear.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.happygear.R;
import com.example.happygear.dto.DescriptionDto;

import java.util.List;

public class ProductDescriptionAdapter extends RecyclerView.Adapter<ProductDescriptionAdapter.ProductDescriptionViewHolder>{

    private List<DescriptionDto> descriptionDtoList;

    public ProductDescriptionAdapter(List<DescriptionDto> descriptionDtoList) {
        this.descriptionDtoList = descriptionDtoList;
    }

    @NonNull
    @Override
    public ProductDescriptionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_description, parent, false);
        return new ProductDescriptionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductDescriptionViewHolder holder, int position) {
        DescriptionDto descriptionDto = descriptionDtoList.get(position);
        if(descriptionDto == null) return;

        holder.productDescription.setText(descriptionDto.getDescription());
        holder.productDescriptionValue.setText(descriptionDto.getValue());
    }

    @Override
    public int getItemCount() {
        if (descriptionDtoList != null)
            return descriptionDtoList.size();
        return 0;
    }

    public class ProductDescriptionViewHolder extends RecyclerView.ViewHolder {

        private TextView productDescription;
        private TextView productDescriptionValue;

        public ProductDescriptionViewHolder(@NonNull View itemView) {
            super(itemView);

            productDescription = itemView.findViewById(R.id.detail_product_description);
            productDescriptionValue = itemView.findViewById(R.id.detail_product_value);
        }
    }
}
