package com.adybelli.android.Object;

public class UpdateUserInfoBody {
    private String user_id;
    private String name;
    private String surname;
    private String phone;
    private String gender;
    private String email;
    private String date_birth;

    public UpdateUserInfoBody(String user_id, String name, String surname, String phone, String gender, String email, String date_birth) {
        this.user_id = user_id;
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.gender = gender;
        this.email = email;
        this.date_birth = date_birth;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDate_birth() {
        return date_birth;
    }

    public void setDate_birth(String date_birth) {
        this.date_birth = date_birth;
    }
}
