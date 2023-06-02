package com.example.gearmobile.models;


public class OrderDetail {
    private Integer detailId;
    private Double price;
    private Integer quantity;
    private Boolean status;
    private Order order;
    private Product product;

    public OrderDetail() {
    }

    public OrderDetail(Integer detailId, Double price, Integer quantity, Boolean status, Order order, Product product) {
        this.detailId = detailId;
        this.price = price;
        this.quantity = quantity;
        this.status = status;
        this.order = order;
        this.product = product;
    }

    public Integer getDetailId() {
        return detailId;
    }

    public void setDetailId(Integer detailId) {
        this.detailId = detailId;
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

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
