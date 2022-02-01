package com.adybelli.android.Post;

public class AddFavPost {
    private String prod_id;

    public AddFavPost(String prod_id) {
        this.prod_id = prod_id;
    }

    public String getProd_id() {
        return prod_id;
    }

    public void setProd_id(String prod_id) {
        this.prod_id = prod_id;
    }
}
