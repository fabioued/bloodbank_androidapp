package com.donars.srp.bloodbank.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Lakshman on 10/12/2016.
 */

public class Hospital {
    //[{"name":"sai","username":"sai","password":"sai","chiefdoctor":"sai","phone":"52525252","address":"saiasi","latitude":"0.00","longitude":"0.00"}]
    private String name,username,password,chiefdoctor,phone,address,latitude,longitude;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getChiefdoctor() {
        return chiefdoctor;
    }

    public void setChiefdoctor(String chiefdoctor) {
        this.chiefdoctor = chiefdoctor;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public Hospital(String name, String username, String password, String chiefdoctor, String phone, String address, String latitude, String longitude) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.chiefdoctor = chiefdoctor;
        this.phone = phone;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
