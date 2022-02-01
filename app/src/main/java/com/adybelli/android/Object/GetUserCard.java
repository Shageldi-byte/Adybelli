package com.adybelli.android.Object;

import java.util.ArrayList;

public class GetUserCard {
    private boolean error;
    private ArrayList<GetUserCardBody> body;

    public GetUserCard(boolean error, ArrayList<GetUserCardBody> body) {
        this.error = error;
        this.body = body;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public ArrayList<GetUserCardBody> getBody() {
        return body;
    }

    public void setBody(ArrayList<GetUserCardBody> body) {
        this.body = body;
    }
}
