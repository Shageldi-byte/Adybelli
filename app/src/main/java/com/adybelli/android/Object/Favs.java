package com.adybelli.android.Object;

import com.adybelli.android.CategoryModel.Size;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Favs {
    @SerializedName("prod_id")
    private int id;
    private String name;
    private String name_ru;
    private String desc;
    @SerializedName("price")
    private Double oldCost;
    @SerializedName("sale_price")
    private Double cost;
    private boolean on_sale;
    private int in_stock;
    private int gender;
    @SerializedName("image")
    private String imageUrl;
    private Trademark trademark;
    private int fav_id;
    private ArrayList<Size> sizes=new ArrayList<>();
    private Integer selected;

    public Favs(int id, String name, String name_ru, String desc, Double oldCost, Double cost, boolean on_sale, int in_stock, int gender, String imageUrl, Trademark trademark, int fav_id, ArrayList<Size> sizes, Integer selected) {
        this.id = id;
        this.name = name;
        this.name_ru = name_ru;
        this.desc = desc;
        this.oldCost = oldCost;
        this.cost = cost;
        this.on_sale = on_sale;
        this.in_stock = in_stock;
        this.gender = gender;
        this.imageUrl = imageUrl;
        this.trademark = trademark;
        this.fav_id = fav_id;
        this.sizes = sizes;
        this.selected = selected;
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

    public String getName_ru() {
        return name_ru;
    }

    public void setName_ru(String name_ru) {
        this.name_ru = name_ru;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Double getOldCost() {
        return oldCost;
    }

    public void setOldCost(Double oldCost) {
        this.oldCost = oldCost;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public boolean isOn_sale() {
        return on_sale;
    }

    public void setOn_sale(boolean on_sale) {
        this.on_sale = on_sale;
    }

    public int getIn_stock() {
        return in_stock;
    }

    public void setIn_stock(int in_stock) {
        this.in_stock = in_stock;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Trademark getTrademark() {
        return trademark;
    }

    public void setTrademark(Trademark trademark) {
        this.trademark = trademark;
    }

    public int getFav_id() {
        return fav_id;
    }

    public void setFav_id(int fav_id) {
        this.fav_id = fav_id;
    }

    public ArrayList<Size> getSizes() {
        return sizes;
    }

    public void setSizes(ArrayList<Size> sizes) {
        this.sizes = sizes;
    }

    public Integer getSelected() {
        return selected;
    }

    public void setSelected(Integer selected) {
        this.selected = selected;
    }
}
