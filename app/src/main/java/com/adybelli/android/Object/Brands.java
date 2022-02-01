package com.adybelli.android.Object;

import com.google.gson.annotations.SerializedName;

public class Brands {
    private int tm_id;
    @SerializedName("title")
    private String name;
    @SerializedName("logo")
    private String imageUrl;


    public Brands(int tm_id, String name, String imageUrl) {
        this.tm_id = tm_id;
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public int getTm_id() {
        return tm_id;
    }

    public void setTm_id(int tm_id) {
        this.tm_id = tm_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
