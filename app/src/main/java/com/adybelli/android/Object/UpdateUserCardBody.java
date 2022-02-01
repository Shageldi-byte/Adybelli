package com.adybelli.android.Object;

public class UpdateUserCardBody {
    private String result;
    private String type;

    public UpdateUserCardBody(String result, String type) {
        this.result = result;
        this.type = type;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
