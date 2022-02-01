package com.adybelli.android.Object;

import java.util.ArrayList;
import java.util.List;

public class Body {
    private List<CarouselListObject> banners=new ArrayList<>();
    private List<ShopByGender> genders=new ArrayList<>();
    private List<PopularCategories> popular_categories=new ArrayList<>();
    private List<TopSold> top_sold=new ArrayList<>();
    private List<TopSold> new_arrival=new ArrayList<>();
    private List<Brands> brands=new ArrayList<>();
    private ArrayList<Constants> constants=new ArrayList<>();
    private String required_version;

    public Body(List<CarouselListObject> banners, List<ShopByGender> genders, List<PopularCategories> popular_categories, List<TopSold> top_sold, List<TopSold> new_arrival, List<Brands> brands, ArrayList<Constants> constants, String required_version) {
        this.banners = banners;
        this.genders = genders;
        this.popular_categories = popular_categories;
        this.top_sold = top_sold;
        this.new_arrival = new_arrival;
        this.brands = brands;
        this.constants = constants;
        this.required_version = required_version;
    }

    public List<CarouselListObject> getBanners() {
        return banners;
    }

    public void setBanners(List<CarouselListObject> banners) {
        this.banners = banners;
    }

    public List<ShopByGender> getGenders() {
        return genders;
    }

    public void setGenders(List<ShopByGender> genders) {
        this.genders = genders;
    }

    public List<PopularCategories> getPopular_categories() {
        return popular_categories;
    }

    public void setPopular_categories(List<PopularCategories> popular_categories) {
        this.popular_categories = popular_categories;
    }

    public List<TopSold> getTop_sold() {
        return top_sold;
    }

    public void setTop_sold(List<TopSold> top_sold) {
        this.top_sold = top_sold;
    }

    public List<TopSold> getNew_arrival() {
        return new_arrival;
    }

    public void setNew_arrival(List<TopSold> new_arrival) {
        this.new_arrival = new_arrival;
    }

    public List<Brands> getBrands() {
        return brands;
    }

    public void setBrands(List<Brands> brands) {
        this.brands = brands;
    }

    public ArrayList<Constants> getConstants() {
        return constants;
    }

    public void setConstants(ArrayList<Constants> constants) {
        this.constants = constants;
    }

    public String getRequired_version() {
        return required_version;
    }

    public void setRequired_version(String required_version) {
        this.required_version = required_version;
    }
}
