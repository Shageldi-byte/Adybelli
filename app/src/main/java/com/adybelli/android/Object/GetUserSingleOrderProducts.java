package com.adybelli.android.Object;

public class GetUserSingleOrderProducts {
    private String image;
    private String size;
    private Integer count;
    private Double price;
    private String name;
    private String name_ru;
    private String trademark;
    private String status;
    private Integer od_id;

    public GetUserSingleOrderProducts(String image, String size, Integer count, Double price, String name, String name_ru, String trademark, String status, Integer od_id) {
        this.image = image;
        this.size = size;
        this.count = count;
        this.price = price;
        this.name = name;
        this.name_ru = name_ru;
        this.trademark = trademark;
        this.status = status;
        this.od_id = od_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
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

    public String getTrademark() {
        return trademark;
    }

    public void setTrademark(String trademark) {
        this.trademark = trademark;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getOd_id() {
        return od_id;
    }

    public void setOd_id(Integer od_id) {
        this.od_id = od_id;
    }
}
