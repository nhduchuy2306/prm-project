package com.example.happygear.dto;

public class OrderDetailModel {

    private Integer detailId;
    private Integer orderId;
    private Double price;
    private Integer quantity;
    private Integer productId;
    private Boolean status;
    private String productName;
    private String insuranceInfo;
    private String picture;

    public OrderDetailModel() {
    }

    public OrderDetailModel(Integer detailId, Integer orderId, Double price, Integer quantity, Integer productId, Boolean status, String productName, String insuranceInfo, String picture) {
        this.detailId = detailId;
        this.orderId = orderId;
        this.price = price;
        this.quantity = quantity;
        this.productId = productId;
        this.status = status;
        this.productName = productName;
        this.insuranceInfo = insuranceInfo;
        this.picture = picture;
    }

    public Integer getDetailId() {
        return detailId;
    }

    public void setDetailId(Integer detailId) {
        this.detailId = detailId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
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

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
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

    @Override
    public String toString() {
        return "OrderDetailModel{" +
                "detailId=" + detailId +
                ", orderId=" + orderId +
                ", price=" + price +
                ", quantity=" + quantity +
                ", productId=" + productId +
                ", status=" + status +
                ", productName='" + productName + '\'' +
                ", insuranceInfo='" + insuranceInfo + '\'' +
                ", picture='" + picture + '\'' +
                '}';
    }
}
