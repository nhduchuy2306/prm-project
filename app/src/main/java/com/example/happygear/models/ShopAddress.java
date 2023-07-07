package com.example.happygear.models;

public class ShopAddress {
    private Integer shopAddressId;
    private String address;
    private String longitude;
    private String latitude;

    public ShopAddress() {
    }

    public ShopAddress(Integer shopAddressId, String address, String longitude, String latitude) {
        this.shopAddressId = shopAddressId;
        this.address = address;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public Integer getShopAddressId() {
        return shopAddressId;
    }

    public void setShopAddressId(Integer shopAddressId) {
        this.shopAddressId = shopAddressId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    @Override
    public String toString() {
        return "ShopAddress{" +
                "shopAddressId=" + shopAddressId +
                ", address='" + address + '\'' +
                ", longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                '}';
    }
}
