package com.adybelli.android.Object;

public class ConstantResponse {
    private boolean error;
    private ConstantResponseBody body;

    public ConstantResponse(boolean error, ConstantResponseBody body) {
        this.error = error;
        this.body = body;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public ConstantResponseBody getBody() {
        return body;
    }

    public void setBody(ConstantResponseBody body) {
        this.body = body;
    }
}
