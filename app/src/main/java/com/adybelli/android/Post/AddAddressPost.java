package com.adybelli.android.Post;

public class AddAddressPost {
    private String ua_id;
    private String title;
    private String address;
    private String phone;
    private String loc_id;
    private String sub_loc_id;

    public AddAddressPost(String ua_id, String title, String address, String phone, String loc_id, String sub_loc_id) {
        this.ua_id = ua_id;
        this.title = title;
        this.address = address;
        this.phone = phone;
        this.loc_id = loc_id;
        this.sub_loc_id = sub_loc_id;
    }

    public String getUa_id() {
        return ua_id;
    }

    public void setUa_id(String ua_id) {
        this.ua_id = ua_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLoc_id() {
        return loc_id;
    }

    public void setLoc_id(String loc_id) {
        this.loc_id = loc_id;
    }

    public String getSub_loc_id() {
        return sub_loc_id;
    }

    public void setSub_loc_id(String sub_loc_id) {
        this.sub_loc_id = sub_loc_id;
    }
}
