package com.adybelli.android.Object;

public class RemoveFavResponse {
    private boolean error;
    private RemoveFavResponseBody body;

    public RemoveFavResponse(boolean error, RemoveFavResponseBody body) {
        this.error = error;
        this.body = body;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public RemoveFavResponseBody getBody() {
        return body;
    }

    public void setBody(RemoveFavResponseBody body) {
        this.body = body;
    }
}
