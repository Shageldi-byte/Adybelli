package com.adybelli.android.Object;

public class Constants {
    private String img;
    private String link;
    private String lang_code;
    private String page;

    public Constants(String img, String link, String lang_code, String page) {
        this.img = img;
        this.link = link;
        this.lang_code = lang_code;
        this.page = page;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLang_code() {
        return lang_code;
    }

    public void setLang_code(String lang_code) {
        this.lang_code = lang_code;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }
}
