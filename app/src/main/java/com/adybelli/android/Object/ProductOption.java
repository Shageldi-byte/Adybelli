package com.adybelli.android.Object;

public class ProductOption {
    private boolean error;
    private ProductOptionBody body;

    public ProductOption(boolean error, ProductOptionBody body) {
        this.error = error;
        this.body = body;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public ProductOptionBody getBody() {
        return body;
    }

    public void setBody(ProductOptionBody body) {
        this.body = body;
    }
}
