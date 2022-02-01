package com.adybelli.android.Object;

import java.util.List;

public class GetCategory {
    private boolean error;
    private List<Category> body;

    public GetCategory(boolean error, List<Category> body) {
        this.error = error;
        this.body = body;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<Category> getBody() {
        return body;
    }

    public void setBody(List<Category> body) {
        this.body = body;
    }
}
