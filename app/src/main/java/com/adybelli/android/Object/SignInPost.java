package com.adybelli.android.Object;

public class SignInPost {
    private String phone;
    private String code;
    private Integer type;

    public SignInPost(String phone, String code, Integer type) {
        this.phone = phone;
        this.code = code;
        this.type = type;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
