package com.example.happygear.interfaces;

import com.example.happygear.dto.OrderDetailModel;

public interface OrderHistoryListener {
    void onOrderHistoryClick(OrderDetailModel orderDetailModel);
}
