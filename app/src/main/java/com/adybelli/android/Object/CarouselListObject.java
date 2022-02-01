package com.adybelli.android.Object;

import com.google.gson.annotations.SerializedName;

public class CarouselListObject {
    @SerializedName("b_id")
    private int id;
    private String title;
    private String title_ru;
    @SerializedName("text")
    private String desc;
    private String text_ru;
    @SerializedName("img_mobile")
    private String imageUrl;
    private String img_mobile_ru;
    private String src;
    private String label;
    private String label_ru;
    private String img;
    private String img_ru;
    private Integer position;

    public CarouselListObject(int id, String title, String title_ru, String desc, String text_ru, String imageUrl, String img_mobile_ru, String src, String label, String label_ru, String img, String img_ru, Integer position) {
        this.id = id;
        this.title = title;
        this.title_ru = title_ru;
        this.desc = desc;
        this.text_ru = text_ru;
        this.imageUrl = imageUrl;
        this.img_mobile_ru = img_mobile_ru;
        this.src = src;
        this.label = label;
        this.label_ru = label_ru;
        this.img = img;
        this.img_ru = img_ru;
        this.position = position;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle_ru() {
        return title_ru;
    }

    public void setTitle_ru(String title_ru) {
        this.title_ru = title_ru;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getText_ru() {
        return text_ru;
    }

    public void setText_ru(String text_ru) {
        this.text_ru = text_ru;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImg_mobile_ru() {
        return img_mobile_ru;
    }

    public void setImg_mobile_ru(String img_mobile_ru) {
        this.img_mobile_ru = img_mobile_ru;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLabel_ru() {
        return label_ru;
    }

    public void setLabel_ru(String label_ru) {
        this.label_ru = label_ru;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getImg_ru() {
        return img_ru;
    }

    public void setImg_ru(String img_ru) {
        this.img_ru = img_ru;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }
}
