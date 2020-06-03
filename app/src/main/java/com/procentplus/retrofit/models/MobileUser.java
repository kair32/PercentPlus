package com.procentplus.retrofit.models;

import com.google.gson.annotations.SerializedName;

public class MobileUser {

    @SerializedName("phone")
    String phone;
    @SerializedName("password")
    String password;
    @SerializedName("name")
    String name;
    @SerializedName("city")
    String city;

    public MobileUser(String phone, String password) {
        this.phone = phone;
        this.password = password;
    }

    public MobileUser(String phone, String password, String name, String city) {
        this.phone = phone;
        this.password = password;
        this.name = name;
        this.city = city;
    }

    public String getPhone() {
        return phone;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }
}
