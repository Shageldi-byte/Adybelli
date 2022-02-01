package com.adybelli.android.Object;

public class CreateOrderResponseBody {
    private String result;
    private String order_id;

    public CreateOrderResponseBody(String result, String order_id) {
        this.result = result;
        this.order_id = order_id;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }
}
