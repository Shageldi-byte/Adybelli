package com.adybelli.android.Post;

public class CreateAccountResponse {
    private boolean error;
    private CreateAccountResponseBody body;

    public CreateAccountResponse(boolean error, CreateAccountResponseBody body) {
        this.error = error;
        this.body = body;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public CreateAccountResponseBody getBody() {
        return body;
    }

    public void setBody(CreateAccountResponseBody body) {
        this.body = body;
    }
}
