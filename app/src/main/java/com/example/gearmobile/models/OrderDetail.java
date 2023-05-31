package com.example.gearmobile.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetail {
    private Integer detailId;
    private Double price;
    private Integer quantity;
    private Boolean status;
    private Order order;
    private Product product;
}
