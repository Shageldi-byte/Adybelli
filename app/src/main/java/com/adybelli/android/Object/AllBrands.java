package com.adybelli.android.Object;

public class AllBrands {
    private int tm_id;
    private String value;
    private String logo;
    private String title;

    public AllBrands(int tm_id, String value, String logo, String title) {
        this.tm_id = tm_id;
        this.value = value;
        this.logo = logo;
        this.title = title;
    }

    public int getTm_id() {
        return tm_id;
    }

    public void setTm_id(int tm_id) {
        this.tm_id = tm_id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
