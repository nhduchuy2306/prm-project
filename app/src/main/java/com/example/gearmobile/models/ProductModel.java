package com.example.gearmobile.models;

import java.util.List;

public class ProductModel {
    private List<Product> data;
    private int size;

    public ProductModel() {
    }

    public ProductModel(List<Product> data, int size) {
        this.data = data;
        this.size = size;
    }

    public List<Product> getData() {
        return data;
    }

    public void setData(List<Product> data) {
        this.data = data;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "ProductModel{" +
                "data=" + data +
                ", size=" + size +
                '}';
    }
}
