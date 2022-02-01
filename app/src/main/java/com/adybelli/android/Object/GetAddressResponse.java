package com.adybelli.android.Object;

import java.util.ArrayList;

public class GetAddressResponse {
    private boolean error;
    private ArrayList<AddAddressResponseBody> body=new ArrayList<>();

    public GetAddressResponse(boolean error, ArrayList<AddAddressResponseBody> body) {
        this.error = error;
        this.body = body;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public ArrayList<AddAddressResponseBody> getBody() {
        return body;
    }

    public void setBody(ArrayList<AddAddressResponseBody> body) {
        this.body = body;
    }
}
