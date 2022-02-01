package com.adybelli.android.Object;

import java.util.ArrayList;

public class AllBrandsResponse {
    private boolean error;
    private ArrayList<AllBrands> body=new ArrayList<>();

    public AllBrandsResponse(boolean error, ArrayList<AllBrands> body) {
        this.error = error;
        this.body = body;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public ArrayList<AllBrands> getBody() {
        return body;
    }

    public void setBody(ArrayList<AllBrands> body) {
        this.body = body;
    }
}
