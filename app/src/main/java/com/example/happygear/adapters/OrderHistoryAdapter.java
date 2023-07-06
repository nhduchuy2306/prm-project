package com.example.happygear.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.happygear.R;
import com.example.happygear.dto.OrderDetailModel;
import com.example.happygear.interfaces.OrderHistoryListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.OrderHistoryViewHolder> {

    private List<OrderDetailModel> mOrderDetailModelList;
    private OrderHistoryListener mOrderHistoryListener;

    public OrderHistoryAdapter(OrderHistoryListener orderHistoryListener) {
        mOrderHistoryListener = orderHistoryListener;
    }

    public void setmOrderDetailModelList(List<OrderDetailModel> OrderDetailModelList) {
        mOrderDetailModelList = OrderDetailModelList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OrderHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_history, parent, false);
        return new OrderHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHistoryViewHolder holder, int position) {
        OrderDetailModel orderDetailModel = mOrderDetailModelList.get(position);
        if (orderDetailModel == null) {
            return;
        }
        String formattedNumberPrice = String.valueOf(orderDetailModel.getPrice()).replaceAll("\\.0+$", "");
        String formattedNumberTotal = String.valueOf(orderDetailModel.getPrice() * orderDetailModel.getQuantity()).replaceAll("\\.0+$", "");
        Picasso.get().load(orderDetailModel.getPicture()).placeholder(androidx.recyclerview.selection.R.drawable.selection_band_overlay).into(holder.orderHistoryImage);

        holder.orderHistoryItemName.setText(orderDetailModel.getProductName());
        holder.orderHistoryItemPrice.setText("Price: $" + formattedNumberPrice);
        holder.orderHistoryItemQuantity.setText("x" + orderDetailModel.getQuantity());
        holder.orderHistoryTotalPrice.setText("$" + formattedNumberTotal);
        holder.orderAgainButton.setOnClickListener(v -> mOrderHistoryListener.onOrderHistoryClick(orderDetailModel));
    }

    @Override
    public int getItemCount() {
        if (mOrderDetailModelList != null) {
            return mOrderDetailModelList.size();
        }
        return 0;
    }

    public class OrderHistoryViewHolder extends RecyclerView.ViewHolder {

        private ImageView orderHistoryImage;
        private TextView orderHistoryItemName;
        private TextView orderHistoryItemPrice;
        private TextView orderHistoryItemQuantity;
        private TextView orderHistoryTotalPrice;
        private Button orderAgainButton;

        public OrderHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            orderHistoryImage = itemView.findViewById(R.id.order_history_item_image);
            orderHistoryItemName = itemView.findViewById(R.id.order_history_item_name);
            orderHistoryItemPrice = itemView.findViewById(R.id.order_history_item_price);
            orderHistoryItemQuantity = itemView.findViewById(R.id.order_history_item_quantity);
            orderHistoryTotalPrice = itemView.findViewById(R.id.order_history_item_total_price);
            orderAgainButton = itemView.findViewById(R.id.order_history_item_order_again);
        }
    }
}
