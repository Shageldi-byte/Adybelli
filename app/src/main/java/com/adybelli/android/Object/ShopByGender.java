package com.adybelli.android.Object;

import com.google.gson.annotations.SerializedName;

public class ShopByGender {
    @SerializedName("gender_id")
    private int id;
    private String title;
    private String title_ru;
    @SerializedName("img")
    private String imageUrl;
    private String categories;

    public ShopByGender(int id, String title, String title_ru, String imageUrl, String categories) {
        this.id = id;
        this.title = title;
        this.title_ru = title_ru;
        this.imageUrl = imageUrl;
        this.categories = categories;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }
}
