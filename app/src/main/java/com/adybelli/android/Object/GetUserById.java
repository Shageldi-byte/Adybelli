package com.adybelli.android.Object;

public class GetUserById {
    private boolean error;
    private GetUserByIdBody body;

    public GetUserById(boolean error, GetUserByIdBody body) {
        this.error = error;
        this.body = body;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public GetUserByIdBody getBody() {
        return body;
    }

    public void setBody(GetUserByIdBody body) {
        this.body = body;
    }
}
