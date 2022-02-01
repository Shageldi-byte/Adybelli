package com.adybelli.android.Post;

public class CancelOrder {
    private Integer order_id;

    public CancelOrder(Integer order_id) {
        this.order_id = order_id;
    }


    public Integer getOrder_id() {
        return order_id;
    }

    public void setOrder_id(Integer order_id) {
        this.order_id = order_id;
    }
}
