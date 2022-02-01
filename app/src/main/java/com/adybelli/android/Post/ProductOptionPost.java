package com.adybelli.android.Post;

public class ProductOptionPost {
    private String trademarks;
    private String categories;
    private String sizes;
    private String colors;
    private int is_discount;

    public ProductOptionPost(String trademarks, String categories, String sizes, String colors, int is_discount) {
        this.trademarks = trademarks;
        this.categories = categories;
        this.sizes = sizes;
        this.colors = colors;
        this.is_discount = is_discount;
    }

    public String getTrademarks() {
        return trademarks;
    }

    public void setTrademarks(String trademarks) {
        this.trademarks = trademarks;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String getSizes() {
        return sizes;
    }

    public void setSizes(String sizes) {
        this.sizes = sizes;
    }

    public String getColors() {
        return colors;
    }

    public void setColors(String colors) {
        this.colors = colors;
    }

    public int getIs_discount() {
        return is_discount;
    }

    public void setIs_discount(int is_discount) {
        this.is_discount = is_discount;
    }
}
