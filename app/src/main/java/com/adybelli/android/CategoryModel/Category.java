package com.adybelli.android.CategoryModel;

public class Category {
    private int cat_id;
    private String title;
    private String title_ru;

    public Category(int cat_id, String title, String title_ru) {
        this.cat_id = cat_id;
        this.title = title;
        this.title_ru = title_ru;
    }

    public int getCat_id() {
        return cat_id;
    }

    public void setCat_id(int cat_id) {
        this.cat_id = cat_id;
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
}
