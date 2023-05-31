package com.example.gearmobile.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
}
