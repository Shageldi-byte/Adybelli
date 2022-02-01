package com.adybelli.android.Post;

public class AddToCardPost {
    private int prod_id;
    private int size_id;
    private int count;

    public AddToCardPost(int prod_id, int size_id, int count) {
        this.prod_id = prod_id;
        this.size_id = size_id;
        this.count = count;
    }

    public int getProd_id() {
        return prod_id;
    }

    public void setProd_id(int prod_id) {
        this.prod_id = prod_id;
    }

    public int getSize_id() {
        return size_id;
    }

    public void setSize_id(int size_id) {
        this.size_id = size_id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
