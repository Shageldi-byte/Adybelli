package com.adybelli.android.Object;

import com.adybelli.android.Object.AddAddressResponseBody;
import com.adybelli.android.Object.GetLocationsBody;

import java.util.ArrayList;

public class GetUserCardInfoBody {
    private Double products_total;
    private Double total;
    private String carts;
    private String captcha;
    private ArrayList<GetLocationsBody> locations=new ArrayList<>();
    private ArrayList<AddAddressResponseBody> addresses=new ArrayList<>();

    public GetUserCardInfoBody(Double products_total, Double total, String carts, String captcha, ArrayList<GetLocationsBody> locations, ArrayList<AddAddressResponseBody> addresses) {
        this.products_total = products_total;
        this.total = total;
        this.carts = carts;
        this.captcha = captcha;
        this.locations = locations;
        this.addresses = addresses;
    }

    public Double getProducts_total() {
        return products_total;
    }

    public void setProducts_total(Double products_total) {
        this.products_total = products_total;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getCarts() {
        return carts;
    }

    public void setCarts(String carts) {
        this.carts = carts;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public ArrayList<GetLocationsBody> getLocations() {
        return locations;
    }

    public void setLocations(ArrayList<GetLocationsBody> locations) {
        this.locations = locations;
    }

    public ArrayList<AddAddressResponseBody> getAddresses() {
        return addresses;
    }

    public void setAddresses(ArrayList<AddAddressResponseBody> addresses) {
        this.addresses = addresses;
    }
}
