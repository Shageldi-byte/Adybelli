package com.adybelli.android.Object;

import com.google.gson.annotations.SerializedName;

public class TopSold {
    @SerializedName("prod_id")
    private int id;
    private String name;
    private String name_ru;
    @SerializedName("trademark")
    private String desc;
    @SerializedName("price")
    private Double oldCost;
    @SerializedName("sale_price")
    private Double cost;
    @SerializedName("image")
    private String imageUrl;
    private boolean on_sale;
    private int is_favorite;
    private Integer fav_id;


    public TopSold(int id, String name, String name_ru, String desc, Double oldCost, Double cost, String imageUrl, boolean on_sale, int is_favorite, Integer fav_id) {
        this.id = id;
        this.name = name;
        this.name_ru = name_ru;
        this.desc = desc;
        this.oldCost = oldCost;
        this.cost = cost;
        this.imageUrl = imageUrl;
        this.on_sale = on_sale;
        this.is_favorite = is_favorite;
        this.fav_id = fav_id;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isOn_sale() {
        return on_sale;
    }

    public void setOn_sale(boolean on_sale) {
        this.on_sale = on_sale;
    }

    public int getIs_favorite() {
        return is_favorite;
    }

    public void setIs_favorite(int is_favorite) {
        this.is_favorite = is_favorite;
    }

    public Integer getFav_id() {
        return fav_id;
    }

    public void setFav_id(Integer fav_id) {
        this.fav_id = fav_id;
    }
}
