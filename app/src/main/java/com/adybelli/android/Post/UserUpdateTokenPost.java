package com.adybelli.android.Post;

public class UserUpdateTokenPost {
    private String device_token;

    public UserUpdateTokenPost(String device_token) {
        this.device_token = device_token;
    }

    public String getDevice_token() {
        return device_token;
    }

    public void setDevice_token(String device_token) {
        this.device_token = device_token;
    }
}
