package com.example.gearmobile.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductPicture {
    private Integer pictureId;
    private String pictureUrl;
    private Boolean status;
    private Product product;
}
