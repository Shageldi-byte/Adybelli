package com.adybelli.android.Object;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetUserSingleOrder {
    private String order_id;
    private String created_at;
    private Integer delivery_day;
    private String status;
    private Double total;
    private boolean shipping;
    private Double shipping_price;
    private LocationChildren location;
    private LocationChildren sub_location;
    private ArrayList<GetUserSingleOrderProducts> products=new ArrayList<>();
    private String phone;
    private String addressTitle;
    private String address;
    private Integer payment_method;

    public GetUserSingleOrder(String order_id, String created_at, Integer delivery_day, String status, Double total, boolean shipping, Double shipping_price, LocationChildren location, LocationChildren sub_location, ArrayList<GetUserSingleOrderProducts> products, String phone, String addressTitle, String address, Integer payment_method) {
        this.order_id = order_id;
        this.created_at = created_at;
        this.delivery_day = delivery_day;
        this.status = status;
        this.total = total;
        this.shipping = shipping;
        this.shipping_price = shipping_price;
        this.location = location;
        this.sub_location = sub_location;
        this.products = products;
        this.phone = phone;
        this.addressTitle = addressTitle;
        this.address = address;
        this.payment_method = payment_method;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public Integer getDelivery_day() {
        return delivery_day;
    }

    public void setDelivery_day(Integer delivery_day) {
        this.delivery_day = delivery_day;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public boolean isShipping() {
        return shipping;
    }

    public void setShipping(boolean shipping) {
        this.shipping = shipping;
    }

    public Double getShipping_price() {
        return shipping_price;
    }

    public void setShipping_price(Double shipping_price) {
        this.shipping_price = shipping_price;
    }

    public LocationChildren getLocation() {
        return location;
    }

    public void setLocation(LocationChildren location) {
        this.location = location;
    }

    public LocationChildren getSub_location() {
        return sub_location;
    }

    public void setSub_location(LocationChildren sub_location) {
        this.sub_location = sub_location;
    }

    public ArrayList<GetUserSingleOrderProducts> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<GetUserSingleOrderProducts> products) {
        this.products = products;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddressTitle() {
        return addressTitle;
    }

    public void setAddressTitle(String addressTitle) {
        this.addressTitle = addressTitle;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(Integer payment_method) {
        this.payment_method = payment_method;
    }
}
