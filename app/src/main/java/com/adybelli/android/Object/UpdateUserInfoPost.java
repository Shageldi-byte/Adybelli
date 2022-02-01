package com.adybelli.android.Object;

public class UpdateUserInfoPost {
    private String name;
    private String surname;
    private String phone;
    private String gender;
    private String date_birth;

    public UpdateUserInfoPost(String name, String surname, String phone, String gender, String date_birth) {
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.gender = gender;
        this.date_birth = date_birth;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDate_birth() {
        return date_birth;
    }

    public void setDate_birth(String date_birth) {
        this.date_birth = date_birth;
    }
}
