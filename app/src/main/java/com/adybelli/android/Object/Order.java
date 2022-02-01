package com.adybelli.android.Object;

public class Order {
    private int id;
    private String date;
    private String cost;
    private String status;
    private int count;

    public Order(int id, String date, String cost, String status, int count) {
        this.id = id;
        this.date = date;
        this.cost = cost;
        this.status = status;
        this.count = count;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
