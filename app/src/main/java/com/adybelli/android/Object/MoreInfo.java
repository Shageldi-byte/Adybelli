package com.adybelli.android.Object;

public class MoreInfo {
    private int id;
    private String name;
    private String desc;
    private String oldCost;
    private String cost;
    private String image;

    public MoreInfo(int id, String name, String desc, String oldCost, String cost, String image) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.oldCost = oldCost;
        this.cost = cost;
        this.image = image;
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getOldCost() {
        return oldCost;
    }

    public void setOldCost(String oldCost) {
        this.oldCost = oldCost;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
