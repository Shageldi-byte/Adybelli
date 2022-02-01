package com.adybelli.android.Object;

import java.util.ArrayList;

public class OfflineMainCategory {
    private ArrayList<Category> mainCategories = new ArrayList<>();
    private int id;

    public OfflineMainCategory(ArrayList<Category> mainCategories, int id) {
        this.mainCategories = mainCategories;
        this.id = id;
    }

    public ArrayList<Category> getMainCategories() {
        return mainCategories;
    }

    public void setMainCategories(ArrayList<Category> mainCategories) {
        this.mainCategories = mainCategories;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
