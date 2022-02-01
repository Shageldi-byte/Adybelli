package com.adybelli.android.Object;

public class GetUserCardBody {

    private String delivery_day;
    private String size_id;
    private String prod_id;
    private String cart_id;
    private String image;
    private String name;
    private String name_ru;
    private Double price;
    private Double sale_price;
    private boolean on_sale;
    private int count;
    private String size;

    public GetUserCardBody(String delivery_day, String size_id, String prod_id, String cart_id, String image, String name, String name_ru, Double price, Double sale_price, boolean on_sale, int count, String size) {
        this.delivery_day = delivery_day;
        this.size_id = size_id;
        this.prod_id = prod_id;
        this.cart_id = cart_id;
        this.image = image;
        this.name = name;
        this.name_ru = name_ru;
        this.price = price;
        this.sale_price = sale_price;
        this.on_sale = on_sale;
        this.count = count;
        this.size = size;
    }

    public String getDelivery_day() {
        return delivery_day;
    }

    public void setDelivery_day(String delivery_day) {
        this.delivery_day = delivery_day;
    }

    public String getSize_id() {
        return size_id;
    }

    public void setSize_id(String size_id) {
        this.size_id = size_id;
    }

    public String getProd_id() {
        return prod_id;
    }

    public void setProd_id(String prod_id) {
        this.prod_id = prod_id;
    }

    public String getCart_id() {
        return cart_id;
    }

    public void setCart_id(String cart_id) {
        this.cart_id = cart_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName_ru() {
        return name_ru;
    }

    public void setName_ru(String name_ru) {
        this.name_ru = name_ru;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getSale_price() {
        return sale_price;
    }

    public void setSale_price(Double sale_price) {
        this.sale_price = sale_price;
    }

    public boolean isOn_sale() {
        return on_sale;
    }

    public void setOn_sale(boolean on_sale) {
        this.on_sale = on_sale;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
