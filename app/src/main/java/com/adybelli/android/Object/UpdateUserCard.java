package com.adybelli.android.Object;

public class UpdateUserCard {
    private boolean error;
    private UpdateUserCardBody body;

    public UpdateUserCard(boolean error, UpdateUserCardBody body) {
        this.error = error;
        this.body = body;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public UpdateUserCardBody getBody() {
        return body;
    }

    public void setBody(UpdateUserCardBody body) {
        this.body = body;
    }
}
