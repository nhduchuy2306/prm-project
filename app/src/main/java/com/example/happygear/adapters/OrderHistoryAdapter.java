package com.example.happygear.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.happygear.R;
import com.example.happygear.models.Order;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.OrderHistoryViewHolder>{

    private List<Order> mOrderList;

    private Context mContext;

    public OrderHistoryAdapter(Context context){
        mContext = context;
    }

    public void setOrderList(List<Order> orderList){
        mOrderList = orderList;
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
        Order order = mOrderList.get(position);
        if (order == null){
            return;
        }
        String formattedNumber = String.valueOf(order.getTotal()).replaceAll("\\.0+$", "");

        holder.orderId.setText(order.getOrderId().toString());
        holder.orderDate.setText(order.getDate().toString());
        holder.orderStatus.setText(order.getStatus().toString());
        holder.orderTotal.setText("$"+formattedNumber);
    }

    @Override
    public int getItemCount() {
        if (mOrderList != null){
            return mOrderList.size();
        }
        return 0;
    }

    public class OrderHistoryViewHolder extends RecyclerView.ViewHolder{

        private LinearLayout orderhistoryItemLayout;
        private TextView orderId;
        private TextView orderStatus;
        private TextView orderTotal;
        private TextView orderDate;

        public OrderHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            orderhistoryItemLayout = itemView.findViewById(R.id.order_history_item_layout);
            orderId = itemView.findViewById(R.id.order_id);
            orderStatus = itemView.findViewById(R.id.order_status);
            orderTotal = itemView.findViewById(R.id.order_status);
            orderDate = itemView.findViewById(R.id.order_date);
        }
    }
}
