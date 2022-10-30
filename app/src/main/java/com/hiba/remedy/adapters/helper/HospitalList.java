package com.hiba.remedy.adapters.helper;

public class HospitalList {

    int hospital_id;
    String hospital_name;
    String hospital_specialization;
    String the_state;
    String hospital_image;
    String hospital_phone;
    String hospital_address;
    String hospital_location;
    float hospital_rate;

    public HospitalList(int hospital_id, String hospital_name, String hospital_image,
                    String hospital_specialization,String the_state, String hospital_phone, String hospital_address, String hospital_location, float hospital_rate, int doctor_id) {
        this.hospital_id = hospital_id;
        this.hospital_name = hospital_name;
        this.hospital_image = hospital_image;
        this.hospital_specialization = hospital_specialization;
        this.the_state = the_state;
        this.hospital_phone = hospital_phone;
        this.hospital_rate=hospital_rate;
        this.hospital_address = hospital_address;
        this.hospital_location = hospital_location;
    }

    public int getHospital_id() {
        return hospital_id;
    }
    public String getHospital_name() {
        return hospital_name;
    }
    public String getHospital_image_url() {
        return hospital_image;
    }
    public String getHospital_specialization() {
        return hospital_specialization;
    }
    public String getThe_state() {
        return the_state;
    }
    public String getHospital_phone() {
        return hospital_phone;
    }
    public String getHospital_address() {
        return hospital_address;
    }
    public String getHospital_location() {
        return hospital_location;
    }
    public float getHospital_rate() {
        return  hospital_rate;
    }
}