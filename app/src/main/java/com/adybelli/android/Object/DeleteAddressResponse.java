package com.adybelli.android.Object;

public class DeleteAddressResponse {
    private boolean error;
    private String body;

    public DeleteAddressResponse(boolean error, String body) {
        this.error = error;
        this.body = body;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
