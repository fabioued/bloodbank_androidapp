package com.donars.srp.bloodbank.model;

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
            drinking_habit,
            date_of_birth,
            age,
            weight,
            no_of_times_donated,
            address,
            lastdonation,
            rating,
            count;

    public UserStats(String name, String username, String password, String gender, String bloodgroup, String phone, String drinking_habit, String date_of_birth, String age, String weight, String no_of_times_donated, String address, String lastdonation, String rating, String count) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.gender = gender;
        this.bloodgroup = bloodgroup;
        this.phone = phone;
        this.drinking_habit = drinking_habit;
        this.date_of_birth = date_of_birth;
        this.age = age;
        this.weight = weight;
        this.no_of_times_donated = no_of_times_donated;
        this.address = address;
        this.lastdonation = lastdonation;
        this.rating = rating;
        this.count = count;
    }

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

    public String getDrinking_habit() {
        return drinking_habit;
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

    public String getNo_of_times_donated() {
        return no_of_times_donated;
    }

    public String getAddress() {
        return address;
    }

    public String getLastdonation() {
        return lastdonation;
    }

    public String getRating() {
        return rating;
    }

    public String getCount() {
        return count;
    }
}
