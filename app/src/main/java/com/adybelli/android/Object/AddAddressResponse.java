package com.adybelli.android.Object;

public class AddAddressResponse {
    private boolean error;
    private AddAddressResponseBody body;


    public AddAddressResponse(boolean error, AddAddressResponseBody body) {
        this.error = error;
        this.body = body;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public AddAddressResponseBody getBody() {
        return body;
    }

    public void setBody(AddAddressResponseBody body) {
        this.body = body;
    }
}
