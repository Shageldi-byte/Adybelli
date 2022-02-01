package com.adybelli.android.Post;

public class CreateAccountPost {
    private String name;
    private String surname;
    private String phone;
    private int gender;
    private String code;


    public CreateAccountPost(String name, String surname, String phone, int gender, String code) {
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.gender = gender;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
