package com.adybelli.android.Object;

import java.util.ArrayList;

public class GetUserOrdersResponseBody {
    private String order_id;
    private String created_at;
    private Integer delivery_day;
    private String status;
    private Double total;
    private boolean shipping;
    private Double shipping_price;
    private ArrayList<GetUserOrderProducts> products;

    public GetUserOrdersResponseBody(String order_id, String created_at, Integer delivery_day, String status, Double total, boolean shipping, Double shipping_price, ArrayList<GetUserOrderProducts> products) {
        this.order_id = order_id;
        this.created_at = created_at;
        this.delivery_day = delivery_day;
        this.status = status;
        this.total = total;
        this.shipping = shipping;
        this.shipping_price = shipping_price;
        this.products = products;
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

    public ArrayList<GetUserOrderProducts> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<GetUserOrderProducts> products) {
        this.products = products;
    }
}
