package com.adybelli.android.Object;

import com.adybelli.android.CategoryModel.Color;

import java.util.ArrayList;

public class SingleProductBody {
    private boolean is_favorite;
    private int prod_id;
    private String name;
    private String name_ru;
    private Double sale_price;
    private Double price;
    private boolean on_sale;
    private boolean in_stock;
    private int gender;
    private String cross_item;
    private ArrayList<ColorVariants> colors=new ArrayList<>();
    private ArrayList<SingleProductSize> sizes=new ArrayList<>();
    private ArrayList<SingleProductDesc> descriptions=new ArrayList<>();
    private ArrayList<SingleProductImages> images=new ArrayList<>();
    private Brands trademarks;
    private Color color;
    private Integer delivery_day;
    private Integer free_cargo;

    public SingleProductBody(boolean is_favorite, int prod_id, String name, String name_ru, Double sale_price, Double price, boolean on_sale, boolean in_stock, int gender, String cross_item, ArrayList<ColorVariants> colors, ArrayList<SingleProductSize> sizes, ArrayList<SingleProductDesc> descriptions, ArrayList<SingleProductImages> images, Brands trademarks, Color color, Integer delivery_day, Integer free_cargo) {
        this.is_favorite = is_favorite;
        this.prod_id = prod_id;
        this.name = name;
        this.name_ru = name_ru;
        this.sale_price = sale_price;
        this.price = price;
        this.on_sale = on_sale;
        this.in_stock = in_stock;
        this.gender = gender;
        this.cross_item = cross_item;
        this.colors = colors;
        this.sizes = sizes;
        this.descriptions = descriptions;
        this.images = images;
        this.trademarks = trademarks;
        this.color = color;
        this.delivery_day = delivery_day;
        this.free_cargo = free_cargo;
    }

    public boolean isIs_favorite() {
        return is_favorite;
    }

    public void setIs_favorite(boolean is_favorite) {
        this.is_favorite = is_favorite;
    }

    public int getProd_id() {
        return prod_id;
    }

    public void setProd_id(int prod_id) {
        this.prod_id = prod_id;
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

    public Double getSale_price() {
        return sale_price;
    }

    public void setSale_price(Double sale_price) {
        this.sale_price = sale_price;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public boolean isOn_sale() {
        return on_sale;
    }

    public void setOn_sale(boolean on_sale) {
        this.on_sale = on_sale;
    }

    public boolean isIn_stock() {
        return in_stock;
    }

    public void setIn_stock(boolean in_stock) {
        this.in_stock = in_stock;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getCross_item() {
        return cross_item;
    }

    public void setCross_item(String cross_item) {
        this.cross_item = cross_item;
    }

    public ArrayList<ColorVariants> getColors() {
        return colors;
    }

    public void setColors(ArrayList<ColorVariants> colors) {
        this.colors = colors;
    }

    public ArrayList<SingleProductSize> getSizes() {
        return sizes;
    }

    public void setSizes(ArrayList<SingleProductSize> sizes) {
        this.sizes = sizes;
    }

    public ArrayList<SingleProductDesc> getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(ArrayList<SingleProductDesc> descriptions) {
        this.descriptions = descriptions;
    }

    public ArrayList<SingleProductImages> getImages() {
        return images;
    }

    public void setImages(ArrayList<SingleProductImages> images) {
        this.images = images;
    }

    public Brands getTrademarks() {
        return trademarks;
    }

    public void setTrademarks(Brands trademarks) {
        this.trademarks = trademarks;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Integer getDelivery_day() {
        return delivery_day;
    }

    public void setDelivery_day(Integer delivery_day) {
        this.delivery_day = delivery_day;
    }

    public Integer getFree_cargo() {
        return free_cargo;
    }

    public void setFree_cargo(Integer free_cargo) {
        this.free_cargo = free_cargo;
    }
}
