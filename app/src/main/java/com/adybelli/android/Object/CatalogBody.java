package com.adybelli.android.Object;

import java.util.List;

public class CatalogBody {
    private int active;
    private List<MainCategory> categories;
    private List<Category> subcategories;

    public CatalogBody(int active, List<MainCategory> categories, List<Category> subcategories) {
        this.active = active;
        this.categories = categories;
        this.subcategories = subcategories;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public List<MainCategory> getCategories() {
        return categories;
    }

    public void setCategories(List<MainCategory> categories) {
        this.categories = categories;
    }

    public List<Category> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(List<Category> subcategories) {
        this.subcategories = subcategories;
    }
}
