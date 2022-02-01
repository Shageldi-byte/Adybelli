package com.adybelli.android.Object;

import com.google.gson.annotations.SerializedName;

public class PopularCategories {
    private int cat_id;
    @SerializedName("title")
    private String name;
    private String title_ru;
    @SerializedName("img")
    private String imageUrl;

    public PopularCategories(int cat_id, String name, String title_ru, String imageUrl) {
        this.cat_id = cat_id;
        this.name = name;
        this.title_ru = title_ru;
        this.imageUrl = imageUrl;
    }

    public int getCat_id() {
        return cat_id;
    }

    public void setCat_id(int cat_id) {
        this.cat_id = cat_id;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
