package com.example.gearmobile.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String email;
    private String username;
    private String fullName;
    private String password;
    private String address;
    private String phoneNumber;
    private Boolean status;
    private Boolean gender;
}
