package com.adybelli.android.Object;

import java.util.ArrayList;

public class GetLocationsBody {
    private String loc_id;
    private String text;
    private String text_ru;
    private String zipcode;
    private String parent_id;
    private int priority;
    private String longtitude;
    private String latitude;
    private Double cargo_price;
    private ArrayList<LocationChildren> children=new ArrayList<>();


    public GetLocationsBody(String loc_id, String text, String text_ru, String zipcode, String parent_id, int priority, String longtitude, String latitude, Double cargo_price, ArrayList<LocationChildren> children) {
        this.loc_id = loc_id;
        this.text = text;
        this.text_ru = text_ru;
        this.zipcode = zipcode;
        this.parent_id = parent_id;
        this.priority = priority;
        this.longtitude = longtitude;
        this.latitude = latitude;
        this.cargo_price = cargo_price;
        this.children = children;
    }

    public String getLoc_id() {
        return loc_id;
    }

    public void setLoc_id(String loc_id) {
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

    public String getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(String longtitude) {
        this.longtitude = longtitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public Double getCargo_price() {
        return cargo_price;
    }

    public void setCargo_price(Double cargo_price) {
        this.cargo_price = cargo_price;
    }

    public ArrayList<LocationChildren> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<LocationChildren> children) {
        this.children = children;
    }
}
