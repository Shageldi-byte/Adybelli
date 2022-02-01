package com.adybelli.android.Object;

public class SignInResponse {
    private boolean error;
    private SignInBody body;

    public SignInResponse(boolean error, SignInBody body) {
        this.error = error;
        this.body = body;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public SignInBody getBody() {
        return body;
    }

    public void setBody(SignInBody body) {
        this.body = body;
    }
}
