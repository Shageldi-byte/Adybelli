package com.adybelli.android.Object;

public class Home {
    private boolean error;
    private Body body;

    public Home(boolean error, Body body) {
        this.error = error;
        this.body = body;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }
}
