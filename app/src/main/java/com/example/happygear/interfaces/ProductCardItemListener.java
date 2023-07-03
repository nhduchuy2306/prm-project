package com.example.happygear.interfaces;

import com.example.happygear.models.Product;

public interface ProductCardItemListener {
    void onCardClick(Product product);
    void addToCart(Product product);
}
