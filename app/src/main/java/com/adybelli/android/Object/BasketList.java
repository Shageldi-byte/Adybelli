package com.adybelli.android.Object;

public class BasketList {
    private int id;
    private String image;
    private String name;
    private String category;
    private String size;
    private String date;
    private String count;
    private String oldCost;
    private String cost;

    public BasketList(int id, String image, String name, String category, String size, String date, String count, String oldCost, String cost) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.category = category;
        this.size = size;
        this.date = date;
        this.count = count;
        this.oldCost = oldCost;
        this.cost = cost;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
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
}
