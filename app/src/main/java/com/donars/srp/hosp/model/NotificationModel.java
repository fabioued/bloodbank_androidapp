package com.donars.srp.hosp.model;

/**
 * Created by Admin1 on 10/15/2016.
 */

public class NotificationModel {
    private String patient_id,patient_name,patient_phone,doctor_name,time_of_submit,hospital_name,time,accepted_status;

    public NotificationModel(String patient_id, String patient_name, String patient_phone, String doctor_name, String time_of_submit, String hospital_name, String time, String accepted_status) {
        this.patient_id = patient_id;
        this.patient_name = patient_name;
        this.patient_phone = patient_phone;
        this.doctor_name = doctor_name;
        this.time_of_submit = time_of_submit;
        this.hospital_name = hospital_name;
        this.time = time;
        this.accepted_status = accepted_status;
    }

    public String getPatient_id() {
        return patient_id;
    }

    public String getPatient_name() {
        return patient_name;
    }

    public String getPatient_phone() {
        return patient_phone;
    }

    public String getDoctor_name() {
        return doctor_name;
    }

    public String getTime_of_submit() {
        return time_of_submit;
    }

    public String getHospital_name() {
        return hospital_name;
    }

    public String getTime() {
        return time;
    }

    public String getAccepted_status() {
        return accepted_status;
    }
}
