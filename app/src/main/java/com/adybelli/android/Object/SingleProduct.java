package com.adybelli.android.Object;

public class SingleProduct {
    private boolean error;
    private SingleProductBody body;

    public SingleProduct(boolean error, SingleProductBody body) {
        this.error = error;
        this.body = body;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public SingleProductBody getBody() {
        return body;
    }

    public void setBody(SingleProductBody body) {
        this.body = body;
    }
}
