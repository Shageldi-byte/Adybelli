package com.adybelli.android.Object;

public class AddressList {
    private int id;
    private String name;
    private String phoneNumber;
    private String address;
    private boolean isActiveAddress;

    public AddressList(int id, String name, String phoneNumber, String address, boolean isActiveAddress) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.isActiveAddress = isActiveAddress;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isActiveAddress() {
        return isActiveAddress;
    }

    public void setActiveAddress(boolean activeAddress) {
        isActiveAddress = activeAddress;
    }
}
