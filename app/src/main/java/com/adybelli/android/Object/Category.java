package com.adybelli.android.Object;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Category {
    @SerializedName("cat_id")
    private int id;
    private String title;
    private String title_ru;
    private String img;
    private List<SubCategory> children;

    public Category(int id, String title, String title_ru, String img, List<SubCategory> children) {
        this.id = id;
        this.title = title;
        this.title_ru = title_ru;
        this.img = img;
        this.children = children;
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

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public List<SubCategory> getChildren() {
        return children;
    }

    public void setChildren(List<SubCategory> children) {
        this.children = children;
    }
}
