package com.adybelli.android.Object;

public class LocationChildren {
    private int loc_id;
    private String text;
    private String text_ru;
    private String zipcode;
    private String parent_id;
    private int priority;

    public LocationChildren(int loc_id, String text, String text_ru, String zipcode, String parent_id, int priority) {
        this.loc_id = loc_id;
        this.text = text;
        this.text_ru = text_ru;
        this.zipcode = zipcode;
        this.parent_id = parent_id;
        this.priority = priority;
    }

    public int getLoc_id() {
        return loc_id;
    }

    public void setLoc_id(int loc_id) {
        this.loc_id = loc_id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText_ru() {
        return text_ru;
    }

    public void setText_ru(String text_ru) {
        this.text_ru = text_ru;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
