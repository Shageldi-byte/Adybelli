package com.adybelli.android.Object;

import java.util.List;

public class GetProducts {
    private boolean error;
    private List<Product> body;

    public GetProducts(boolean error, List<Product> body) {
        this.error = error;
        this.body = body;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<Product> getBody() {
        return body;
    }

    public void setBody(List<Product> body) {
        this.body = body;
    }
}
