package com.adybelli.android.Object;

public class Trademark {
    private int tm_id;
    private String value;
    private String title;

    public Trademark(int tm_id, String value, String title) {
        this.tm_id = tm_id;
        this.value = value;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
