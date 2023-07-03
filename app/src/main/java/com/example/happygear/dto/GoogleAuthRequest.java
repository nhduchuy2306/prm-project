package com.example.happygear.dto;

public class GoogleAuthRequest {
    private String displayName;
    private String email;
    private String phoneNumber;
    private String photoURL;
    private String providerId;
    private String uid;

    public GoogleAuthRequest() {
    }

    public GoogleAuthRequest(String displayName, String email, String phoneNumber, String photoURL, String providerId, String uid) {
        this.displayName = displayName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.photoURL = photoURL;
        this.providerId = providerId;
        this.uid = uid;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
