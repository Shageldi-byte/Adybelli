package com.adybelli.android.Object;

public class AddFavResponse {
    private boolean error;
    private AddFavResponseBody body;

    public AddFavResponse(boolean error, AddFavResponseBody body) {
        this.error = error;
        this.body = body;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public AddFavResponseBody getBody() {
        return body;
    }

    public void setBody(AddFavResponseBody body) {
        this.body = body;
    }
}
