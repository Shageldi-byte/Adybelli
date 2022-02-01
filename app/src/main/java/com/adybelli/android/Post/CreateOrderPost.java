package com.adybelli.android.Post;

public class CreateOrderPost {
    private int shipping;
    private int address_id;
    private int payment_method;
    private String selected_cart_id_list;
    private String captcha;

    public CreateOrderPost(int shipping, int address_id, int payment_method, String selected_cart_id_list, String captcha) {
        this.shipping = shipping;
        this.address_id = address_id;
        this.payment_method = payment_method;
        this.selected_cart_id_list = selected_cart_id_list;
        this.captcha = captcha;
    }

    public int getShipping() {
        return shipping;
    }

    public void setShipping(int shipping) {
        this.shipping = shipping;
    }

    public int getAddress_id() {
        return address_id;
    }

    public void setAddress_id(int address_id) {
        this.address_id = address_id;
    }

    public int getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(int payment_method) {
        this.payment_method = payment_method;
    }

    public String getSelected_cart_id_list() {
        return selected_cart_id_list;
    }

    public void setSelected_cart_id_list(String selected_cart_id_list) {
        this.selected_cart_id_list = selected_cart_id_list;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }
}
