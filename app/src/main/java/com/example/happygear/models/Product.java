package com.example.happygear.models;

import java.io.Serializable;

public class Product implements Serializable {
    private Integer productId;
    private String productName;
    private Double price;
    private Integer quantity;
    private String insuranceInfo;
    private String picture;
    private Boolean status;
    private int brandId;
    private int categoryId;

    public Product() {
    }

    public Product(Integer productId, String productName, Double price, Integer quantity,
                   String insuranceInfo, String picture, Boolean status, int brandId,
                   int categoryId) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.insuranceInfo = insuranceInfo;
        this.picture = picture;
        this.status = status;
        this.brandId = brandId;
        this.categoryId = categoryId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getInsuranceInfo() {
        return insuranceInfo;
    }

    public void setInsuranceInfo(String insuranceInfo) {
        this.insuranceInfo = insuranceInfo;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", productName='" + productName + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", insuranceInfo='" + insuranceInfo + '\'' +
                ", picture='" + picture + '\'' +
                ", status=" + status +
                ", brandId=" + brandId +
                ", categoryId=" + categoryId +
                '}';
    }
}
