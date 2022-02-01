package com.adybelli.android.Post;

public class UpdateUserCardPost {
    private int size_id;
    private int count;
    private int cart_id;

    public UpdateUserCardPost(int size_id, int count, int cart_id) {
        this.size_id = size_id;
        this.count = count;
        this.cart_id = cart_id;
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

    public int getCart_id() {
        return cart_id;
    }

    public void setCart_id(int cart_id) {
        this.cart_id = cart_id;
    }
}
