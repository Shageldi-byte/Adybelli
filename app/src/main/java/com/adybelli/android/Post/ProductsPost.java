package com.adybelli.android.Post;

public class ProductsPost {
    String trademarks;
    String categories;
    String sizes;
    String colors;
    int is_discount;
    int page;
    int per_page;
    int sort;
    Double min;
    Double max;

    public ProductsPost(String trademarks, String categories, String sizes, String colors, int is_discount, int page, int per_page, int sort, Double min, Double max) {
        this.trademarks = trademarks;
        this.categories = categories;
        this.sizes = sizes;
        this.colors = colors;
        this.is_discount = is_discount;
        this.page = page;
        this.per_page = per_page;
        this.sort = sort;
        this.min = min;
        this.max = max;
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

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPer_page() {
        return per_page;
    }

    public void setPer_page(int per_page) {
        this.per_page = per_page;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public Double getMin() {
        return min;
    }

    public void setMin(Double min) {
        this.min = min;
    }

    public Double getMax() {
        return max;
    }

    public void setMax(Double max) {
        this.max = max;
    }
}
