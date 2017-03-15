package com.donars.srp.bloodbank.model;

/**
 * Created by Admin1 on 10/15/2016.
 */

public class NotificationModel {
    private String donor_id,donor_name,patient_id,patient_name,donor_phone,patient_blood_group,time_of_submit,hospital_name,time_of_request,
            time_to_arrive,
            accepted_status;

    public NotificationModel(String donor_id, String donor_name, String patient_id, String patient_name, String donor_phone, String patient_blood_group, String time_of_submit, String hospital_name, String time_of_request, String time_to_arrive, String accepted_status) {

        this.donor_id = donor_id;
        this.donor_name = donor_name;
        this.patient_id = patient_id;
        this.patient_name = patient_name;
        this.donor_phone = donor_phone;
        this.patient_blood_group = patient_blood_group;
        this.time_of_submit = time_of_submit;
        this.hospital_name = hospital_name;
        this.time_of_request = time_of_request;
        this.time_to_arrive = time_to_arrive;
        this.accepted_status = accepted_status;
    }

    public String getDonor_id() {
        return donor_id;
    }

    public String getDonor_name() {
        return donor_name;
    }

    public String getPatient_id() {
        return patient_id;
    }

    public String getPatient_name() {
        return patient_name;
    }

    public String getDonor_phone() {
        return donor_phone;
    }

    public String getPatient_blood_group() {
        return patient_blood_group;
    }

    public String getTime_of_submit() {
        return time_of_submit;
    }

    public String getHospital_name() {
        return hospital_name;
    }

    public String getTime_of_request() {
        return time_of_request;
    }

    public String getTime_to_arrive() {
        return time_to_arrive;
    }

    public String getAccepted_status() {
        return accepted_status;
    }
}
