package com.donars.srp.hosp.model;

/**
 * Created by Lakshman on 10/23/2016.
 */

public class UserStats {
    private String name,
            username,
            password,
            gender,
            bloodgroup,
            phone,
            date_of_birth,
            age,
            weight,
            address;

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getGender() {
        return gender;
    }

    public String getBloodgroup() {
        return bloodgroup;
    }

    public String getPhone() {
        return phone;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public String getAge() {
        return age;
    }

    public String getWeight() {
        return weight;
    }

    public String getAddress() {
        return address;
    }

    public UserStats(String name, String username, String password, String gender, String bloodgroup, String phone, String date_of_birth, String age, String weight, String address) {

        this.name = name;
        this.username = username;
        this.password = password;
        this.gender = gender;
        this.bloodgroup = bloodgroup;
        this.phone = phone;
        this.date_of_birth = date_of_birth;
        this.age = age;
        this.weight = weight;
        this.address = address;
    }
}