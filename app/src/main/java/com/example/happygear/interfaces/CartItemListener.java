package com.example.happygear.interfaces;

import com.example.happygear.dto.CartDto;

public interface CartItemListener {
    void onRemove(CartDto cartDto);
    void onIncrease(CartDto cartDto);
    void onDecrease(CartDto cartDto);
}
