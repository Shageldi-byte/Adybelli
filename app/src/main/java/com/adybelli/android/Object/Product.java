package com.adybelli.android.Object;

import com.google.gson.annotations.SerializedName;

public class    Product {
    @SerializedName("prod_id")
    private Integer id;
    private String name;
    private String name_ru;
    private String desc;
    @SerializedName("price")
    private Double oldCost;
    @SerializedName("sale_price")
    private Double cost;
    private Boolean on_sale;
    private Boolean in_stock;
    private Integer gender;
    @SerializedName("image")
    private String imageUrl;
    private String trademark;
    private Integer fav_id;
    private Integer is_favorite;

    public Product(Integer id, String name, String name_ru, String desc, Double oldCost, Double cost, Boolean on_sale, Boolean in_stock, Integer gender, String imageUrl, String trademark, Integer fav_id, Integer is_favorite) {
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
        this.is_favorite = is_favorite;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public Boolean getOn_sale() {
        return on_sale;
    }

    public void setOn_sale(Boolean on_sale) {
        this.on_sale = on_sale;
    }

    public Boolean getIn_stock() {
        return in_stock;
    }

    public void setIn_stock(Boolean in_stock) {
        this.in_stock = in_stock;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTrademark() {
        return trademark;
    }

    public void setTrademark(String trademark) {
        this.trademark = trademark;
    }

    public Integer getFav_id() {
        return fav_id;
    }

    public void setFav_id(Integer fav_id) {
        this.fav_id = fav_id;
    }

    public Integer getIs_favorite() {
        return is_favorite;
    }

    public void setIs_favorite(Integer is_favorite) {
        this.is_favorite = is_favorite;
    }
}
