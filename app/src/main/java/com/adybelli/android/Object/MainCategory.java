package com.adybelli.android.Object;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MainCategory {
    @SerializedName("cat_id")
    int id;
    @SerializedName("title")
    String name;
    String title_ru;
    @SerializedName("img")
    String image;

    public MainCategory(int id, String name, String title_ru, String image) {
        this.id = id;
        this.name = name;
        this.title_ru = title_ru;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle_ru() {
        return title_ru;
    }

    public void setTitle_ru(String title_ru) {
        this.title_ru = title_ru;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
