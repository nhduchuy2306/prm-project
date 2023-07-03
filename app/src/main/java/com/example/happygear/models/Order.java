package com.example.happygear.models;

import java.sql.Date;

public class Order {
    private Integer orderId;
    private Date date;
    private Double total;
    private Integer status;
    private User user;

    public Order() {
    }

    public Order(Integer orderId, Date date, Double total, Integer status, User user) {
        this.orderId = orderId;
        this.date = date;
        this.total = total;
        this.status = status;
        this.user = user;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
