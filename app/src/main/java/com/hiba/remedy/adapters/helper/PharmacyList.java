package com.hiba.remedy.adapters.helper;

public class PharmacyList {

    private int pharmacy_id;
    private String pharmacy_name;



    private String the_state;
    private String pharmacy_image;
    private String pharmacy_phone;
    private String pharmacy_address;
    private String pharmacy_location;
    private float pharmacy_rate;
    private int hospital_id;

    public PharmacyList( int pharmacy_id,
            String pharmacy_name,
            String the_state,
            String pharmacy_image,
            String pharmacy_phone,
            String pharmacy_address,
            String pharmacy_location,
            float pharmacy_rate,
            int hospital_id) {
        this.pharmacy_id =pharmacy_id ;
        this.hospital_id = hospital_id;
        this.pharmacy_name = pharmacy_name;
        this.pharmacy_image = pharmacy_image;
        this.the_state = the_state;
        this.pharmacy_phone = pharmacy_phone;
        this.pharmacy_location=pharmacy_location;
        this.pharmacy_address = pharmacy_address;
        this.pharmacy_rate = pharmacy_rate;
    }
    public String getThe_state() {
        return the_state;
    }

    public int getHospital_id() {
        return hospital_id;
    }
    public int getPharmacy_id() {
        return pharmacy_id;
    }

    public String getPharmacy_name() {
        return pharmacy_name;
    }

    public String getPharmacy_image() {
        return pharmacy_image;
    }

    public String getPharmacy_phone() {
        return pharmacy_phone;
    }

    public String getPharmacy_address() {
        return pharmacy_address;
    }

    public String getPharmacy_location() {
        return pharmacy_location;
    }

    public float getPharmacy_rate() {
        return pharmacy_rate;
    }
}
