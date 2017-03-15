package com.donars.srp.hosp.model;

/**
 * Created by Lakshman on 10/30/2016.
 */

public class DoctorModel {
    private String doctor_id,name,special,hospital_name,age,phone;

    public String getDoctor_id() {
        return doctor_id;
    }

    public String getName() {
        return name;
    }

    public String getSpecial() {
        return special;
    }

    public String getHospital_name() {
        return hospital_name;
    }

    public String getAge() {
        return age;
    }

    public String getPhone() {
        return phone;
    }

    public DoctorModel(String doctor_id, String name, String special, String hospital_name, String age, String phone) {

        this.doctor_id = doctor_id;
        this.name = name;
        this.special = special;
        this.hospital_name = hospital_name;
        this.age = age;
        this.phone = phone;
    }
}
