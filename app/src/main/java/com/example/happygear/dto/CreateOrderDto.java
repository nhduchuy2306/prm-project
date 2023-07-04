package com.example.happygear.dto;

import java.util.List;

public class CreateOrderDto {
    private String userName;
    private List<CartDto> cartItems;

    public CreateOrderDto() {
    }

    public CreateOrderDto(String userName, List<CartDto> cartItems) {
        this.userName = userName;
        this.cartItems = cartItems;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<CartDto> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartDto> cartItems) {
        this.cartItems = cartItems;
    }
}
