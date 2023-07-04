package com.example.happygear.dto;

public class DescriptionDto {
    private String description;
    private String value;

    public DescriptionDto() {
    }

    public DescriptionDto(String description, String value) {
        this.description = description;
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public String getValue() {
        return value;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
