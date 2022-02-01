package com.adybelli.android.Object;

import java.util.ArrayList;

public class GetLocations {
    private boolean error;
    private ArrayList<GetLocationsBody> body=new ArrayList<>();

    public GetLocations(boolean error, ArrayList<GetLocationsBody> body) {
        this.error = error;
        this.body = body;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public ArrayList<GetLocationsBody> getBody() {
        return body;
    }

    public void setBody(ArrayList<GetLocationsBody> body) {
        this.body = body;
    }
}
