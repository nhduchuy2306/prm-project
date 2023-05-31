package com.example.gearmobile.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Brand {
    private Integer brandId;
    private String brandName;
    private Boolean status;
}
