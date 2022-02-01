package com.adybelli.android.Object;

import java.util.List;

public class GetFavs {
    private boolean error;
    private List<Favs> body;

    public GetFavs(boolean error, List<Favs> body) {
        this.error = error;
        this.body = body;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<Favs> getBody() {
        return body;
    }

    public void setBody(List<Favs> body) {
        this.body = body;
    }
}
