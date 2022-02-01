package com.adybelli.android.Object;

import com.adybelli.android.CategoryModel.Brands;
import com.adybelli.android.CategoryModel.Category;
import com.adybelli.android.CategoryModel.Color;
import com.adybelli.android.CategoryModel.Size;

import java.util.ArrayList;

public class ProductOptionBody {
    private ArrayList<Size> sizes=new ArrayList<>();
    private ArrayList<com.adybelli.android.CategoryModel.Brands> trademarks=new ArrayList<>();
    private ArrayList<com.adybelli.android.CategoryModel.Category> categories=new ArrayList<>();
    private ArrayList<Color> colors=new ArrayList<>();

    public ProductOptionBody(ArrayList<Size> sizes, ArrayList<com.adybelli.android.CategoryModel.Brands> trademarks, ArrayList<com.adybelli.android.CategoryModel.Category> categories, ArrayList<Color> colors) {
        this.sizes = sizes;
        this.trademarks = trademarks;
        this.categories = categories;
        this.colors = colors;
    }

    public ArrayList<Size> getSizes() {
        return sizes;
    }

    public void setSizes(ArrayList<Size> sizes) {
        this.sizes = sizes;
    }

    public ArrayList<com.adybelli.android.CategoryModel.Brands> getTrademarks() {
        return trademarks;
    }

    public void setTrademarks(ArrayList<Brands> trademarks) {
        this.trademarks = trademarks;
    }

    public ArrayList<com.adybelli.android.CategoryModel.Category> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<Category> categories) {
        this.categories = categories;
    }

    public ArrayList<Color> getColors() {
        return colors;
    }

    public void setColors(ArrayList<Color> colors) {
        this.colors = colors;
    }
}
