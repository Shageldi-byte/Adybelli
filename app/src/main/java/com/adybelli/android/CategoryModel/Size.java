package com.adybelli.android.CategoryModel;

import com.google.gson.annotations.SerializedName;

public class Size {
    @SerializedName("s_id")
    private int id;
    @SerializedName("label")
    private String size;
    private Integer selected;
    private Integer stockQuantity;

    public Size(int id, String size, Integer selected, Integer stockQuantity) {
        this.id = id;
        this.size = size;
        this.selected = selected;
        this.stockQuantity = stockQuantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Integer getSelected() {
        return selected;
    }

    public void setSelected(Integer selected) {
        this.selected = selected;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }
}
