package com.adybelli.android.Object;

public class ColorVariants {
    private String image;
    private int prod_id;

    public ColorVariants(String image, int prod_id) {
        this.image = image;
        this.prod_id = prod_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getProd_id() {
        return prod_id;
    }

    public void setProd_id(int prod_id) {
        this.prod_id = prod_id;
    }
}
