package com.adybelli.android.Object;

import java.util.ArrayList;

public class GetUserOrdersResponse {
    private boolean error;
    private ArrayList<GetUserOrdersResponseBody> body;

    public GetUserOrdersResponse(boolean error, ArrayList<GetUserOrdersResponseBody> body) {
        this.error = error;
        this.body = body;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public ArrayList<GetUserOrdersResponseBody> getBody() {
        return body;
    }

    public void setBody(ArrayList<GetUserOrdersResponseBody> body) {
        this.body = body;
    }
}
