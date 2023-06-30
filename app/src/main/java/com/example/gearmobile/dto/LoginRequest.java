package com.example.gearmobile.dto;

import com.google.gson.annotations.SerializedName;

public class LoginRequest {
    private String username;
    private String password;

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
