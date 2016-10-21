package com.donars.srp.bloodbank.model;

import com.google.gson.annotations.SerializedName;

import java.sql.Timestamp;

/**
 * Created by Lakshman on 10/12/2016.
 */

public class User {
    @SerializedName("name")
    String name;
    @SerializedName("username")
    String Username;
    @SerializedName("password")
    String Password;
    @SerializedName("bloodgroup")
    String Blood_GRP;
    @SerializedName("gender")
    String Gender;
    @SerializedName("address")
    String Address;
    @SerializedName("lastdonation")
    String lastdonation;
    String phone;

    public String getLastdonation() {
        return lastdonation;
    }

    public String getPhone() {
        return phone;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return Username;
    }

    public String getPassword() {
        return Password;
    }

    public String getBlood_GRP() {
        return Blood_GRP;
    }

    public String getGender() {
        return Gender;
    }

    public String getAddress() {
        return Address;
    }

    public String getTimestamp() {
        return lastdonation;
    }

    public User(String name, String username, String password, String blood_GRP, String gender, String address, String timestamp,String phone) {
        this.name = name;
        Username = username;
        Password = password;
        Blood_GRP = blood_GRP;
        Gender = gender;
        Address = address;
        lastdonation = timestamp;
        this.phone=phone;
    }
}
