package com.adybelli.android.Post;

public class VerifyUserResponseBody {
    private boolean error;
    private VerifyUserResponse body;

    public VerifyUserResponseBody(boolean error, VerifyUserResponse body) {
        this.error = error;
        this.body = body;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public VerifyUserResponse getBody() {
        return body;
    }

    public void setBody(VerifyUserResponse body) {
        this.body = body;
    }
}
