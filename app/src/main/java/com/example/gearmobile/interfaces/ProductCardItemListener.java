package com.example.gearmobile.interfaces;

import com.example.gearmobile.models.Product;

public interface ProductCardItemListener {
    void onCardClick(Product product);
    void addToCart(Product product);
}
