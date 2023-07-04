package com.example.happygear.dto;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "cart")
public class CartDto {
    @PrimaryKey
    private int productId;
    private int quantity;
    private double price;
    private String productName;
    private String productImage;

    public CartDto() {
    }

    public CartDto(int productId, int quantity, double price, String productName, String productImage) {
        this.productId = productId;
        this.quantity = quantity;
        this.productName = productName;
        this.productImage = productImage;
        this.price = price;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    @Override
    public String toString() {
        return "CartDto{" +
                "productId=" + productId +
                ", quantity=" + quantity +
                ", productName='" + productName + '\'' +
                ", productImage='" + productImage + '\'' +
                '}';
    }
}
