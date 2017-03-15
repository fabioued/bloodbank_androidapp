package com.donars.srp.hosp.model;

/**
 * Created by Admin1 on 10/16/2016.
 */

public class RequestModel {
    private  String Patient_id,Patient_name,Blood_group,Hospital_name,Hosp_address,Latitude,Longitude,quantity;

    public RequestModel(String patient_id, String patient_name, String blood_group, String hospital_name, String hosp_address, String latitude, String longitude, String quantity) {
        Patient_id = patient_id;
        Patient_name = patient_name;
        Blood_group = blood_group;
        Hospital_name = hospital_name;
        Hosp_address = hosp_address;
        Latitude = latitude;
        Longitude = longitude;
        this.quantity = quantity;
    }

    public String getPatient_id() {
        return Patient_id;
    }

    public String getPatient_name() {
        return Patient_name;
    }

    public String getBlood_group() {
        return Blood_group;
    }

    public String getHospital_name() {
        return Hospital_name;
    }

    public String getHosp_address() {
        return Hosp_address;
    }

    public String getLatitude() {
        return Latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public String getQuantity() {
        return quantity;
    }
}
