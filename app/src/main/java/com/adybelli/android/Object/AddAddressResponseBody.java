package com.adybelli.android.Object;

public class AddAddressResponseBody {
    private String ua_id;
    private String user_id;
    private String title;
    private String phone;
    private String address;
    private GetLocationsBody location;
    private GetLocationsBody sub_location;

    public AddAddressResponseBody(String ua_id, String user_id, String title, String phone, String address, GetLocationsBody location, GetLocationsBody sub_location) {
        this.ua_id = ua_id;
        this.user_id = user_id;
        this.title = title;
        this.phone = phone;
        this.address = address;
        this.location = location;
        this.sub_location = sub_location;
    }

    public String getUa_id() {
        return ua_id;
    }

    public void setUa_id(String ua_id) {
        this.ua_id = ua_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public GetLocationsBody getLocation() {
        return location;
    }

    public void setLocation(GetLocationsBody location) {
        this.location = location;
    }

    public GetLocationsBody getSub_location() {
        return sub_location;
    }

    public void setSub_location(GetLocationsBody sub_location) {
        this.sub_location = sub_location;
    }


}
