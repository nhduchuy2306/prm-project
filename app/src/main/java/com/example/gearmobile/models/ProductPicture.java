package com.example.gearmobile.models;


public class ProductPicture {
    private Integer pictureId;
    private String pictureUrl;
    private Boolean status;
    private Product product;

    public ProductPicture() {
    }

    public ProductPicture(Integer pictureId, String pictureUrl, Boolean status, Product product) {
        this.pictureId = pictureId;
        this.pictureUrl = pictureUrl;
        this.status = status;
        this.product = product;
    }

    public Integer getPictureId() {
        return pictureId;
    }

    public void setPictureId(Integer pictureId) {
        this.pictureId = pictureId;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
