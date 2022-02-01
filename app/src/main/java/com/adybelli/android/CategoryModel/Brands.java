package com.adybelli.android.CategoryModel;

public class Brands {
    private int tm_id;
    private String title;

    public Brands(int tm_id, String title) {
        this.tm_id = tm_id;
        this.title = title;
    }

    public int getTm_id() {
        return tm_id;
    }

    public void setTm_id(int tm_id) {
        this.tm_id = tm_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
