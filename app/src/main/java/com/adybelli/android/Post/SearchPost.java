package com.adybelli.android.Post;

public class SearchPost {
    private int page;
    private int per_page;
    private String text;

    public SearchPost(int page, int per_page, String text) {
        this.page = page;
        this.per_page = per_page;
        this.text = text;
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
