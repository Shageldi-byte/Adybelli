package com.adybelli.android.Object;

public class Catalog {
    private boolean error;
    private CatalogBody body;

    public Catalog(boolean error, CatalogBody body) {
        this.error = error;
        this.body = body;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public CatalogBody getBody() {
        return body;
    }

    public void setBody(CatalogBody body) {
        this.body = body;
    }
}
