package com.adybelli.android.Object;

public class AddToCardResponse {
    private boolean error;
    private String body;

    public AddToCardResponse(boolean error, String body) {
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
