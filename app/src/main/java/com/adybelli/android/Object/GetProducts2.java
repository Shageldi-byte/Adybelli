package com.adybelli.android.Object;


import java.util.List;

public class GetProducts2 {
    private boolean error;
    private List<TopSold> body;

    public GetProducts2(boolean error, List<TopSold> body) {
        this.error = error;
        this.body = body;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<TopSold> getBody() {
        return body;
    }

    public void setBody(List<TopSold> body) {
        this.body = body;
    }
}
