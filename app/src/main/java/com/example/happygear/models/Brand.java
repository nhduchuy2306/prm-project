package com.example.happygear.models;

public class Brand {
    private Integer brandId;
    private String brandName;
    private Boolean status;

    public Brand() {}

    public Brand(Integer brandId, String brandName, Boolean status) {
        this.brandId = brandId;
        this.brandName = brandName;
        this.status = status;
    }

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
