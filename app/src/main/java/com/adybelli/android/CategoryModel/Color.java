package com.adybelli.android.CategoryModel;

import com.google.gson.annotations.SerializedName;

public class Color {
    @SerializedName("pc_id")
    private int id;
    @SerializedName("color")
    private String name;
    private String color_ru;
    @SerializedName("color_hex")
    private String dexColor;

    public Color(int id, String name, String color_ru, String dexColor) {
        this.id = id;
        this.name = name;
        this.color_ru = color_ru;
        this.dexColor = dexColor;
    }

    public int getId() {    
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor_ru() {
        return color_ru;
    }

    public void setColor_ru(String color_ru) {
        this.color_ru = color_ru;
    }

    public String getDexColor() {
        return dexColor;
    }

    public void setDexColor(String dexColor) {
        this.dexColor = dexColor;
    }
}
