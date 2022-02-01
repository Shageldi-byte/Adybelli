package com.adybelli.android.Object;

public class GetUserSingleOrderResponse {
    private boolean error;
    private GetUserSingleOrder body;

    public GetUserSingleOrderResponse(boolean error, GetUserSingleOrder body) {
        this.error = error;
        this.body = body;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public GetUserSingleOrder getBody() {
        return body;
    }

    public void setBody(GetUserSingleOrder body) {
        this.body = body;
    }
}
