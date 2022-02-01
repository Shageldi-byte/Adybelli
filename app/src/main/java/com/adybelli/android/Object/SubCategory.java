package com.adybelli.android.Object;

import com.google.gson.annotations.SerializedName;

public class SubCategory {
    @SerializedName("cat_id")
    private int id;
    private String title;
    private String title_ru;
    @SerializedName("img")
    private String image;
    private Integer type;


    public SubCategory(int id, String title, String title_ru, String image, Integer type) {
        this.id = id;
        this.title = title;
        this.title_ru = title_ru;
        this.image = image;
        this.type = type;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
