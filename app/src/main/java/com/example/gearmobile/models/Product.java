package com.example.gearmobile.models;


public class Product {
    private Integer productId;
    private String productName;
    private Double price;
    private Integer quantity;
    private String insuranceInfo;
    private String picture;
    private Boolean status;
    private Brand brand;
    private Category category;
    private ProductDescription description;

    public Product(Integer productId, String productName, Double price, Integer quantity, String insuranceInfo, String picture, Boolean status, Brand brand, Category category, ProductDescription description) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.insuranceInfo = insuranceInfo;
        this.picture = picture;
        this.status = status;
        this.brand = brand;
        this.category = category;
        this.description = description;
    }

    public Product() {
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

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public ProductDescription getDescription() {
        return description;
    }

    public void setDescription(ProductDescription description) {
        this.description = description;
    }
}
