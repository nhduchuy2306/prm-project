package com.example.gearmobile.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDescription {
    private Integer productId;
    private String keycap;
    private String switchKeyBoard;
    private String typeKeyboard;
    private String connect;
    private String led;
    private String freigh;
    private String itemDimension;
    private String cpu;
    private String ram;
    private String operatingSystem;
    private String battery;
    private String hardDisk;
    private String graphicCard;
    private String keyBoard;
    private String audio;
    private String wifi;
    private String bluetooth;
    private String color;
    private String frameRate;
    private String screenSize;
    private String screenType;
    private Category category;
    private Product product;
}
