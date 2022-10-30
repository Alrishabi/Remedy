package com.hiba.remedy.adapters.helper;

public class LabsList {

    int lap_id;
    String lap_name;
    String lap_image;
    String lap_specialization;
    String the_state;
    String lap_phone;
    float lap_rate;
    String lap_address;
    String lap_location;

//    {"lap_id":"1",
//    "lap_name":"\u0625\u0633\u062a\u0627\u0643",
//    "lap_image":"\u0625\u0633\u062a\u0627\u0643",
//    "lap_specialization":"\u062c\u0645\u064a\u0639 \u062a\u062e\u0627\u0644\u064a\u0644 \u0627\u0644\u062f\u0645 \u0648\u0627\u0644\u0635\u0648\u0631 \u0627\u0644\u0645\u0642\u0637\u0639\u064a\u0629",
//    "lap_phone":"123456789",
//    "lap_rate":"4.5",
//    "lap_address":"\u0627\u0644\u062e\u0631\u0637\u0648\u0645 \u0634\u0627\u0631\u0639 \u0627\u0644\u0642\u064a\u0627\u062f\u0647",
//    "lap_location":"51841842244\u0631\u064216641616"}]}
    public LabsList(int lap_id,
                    String lap_name,
                    String lap_image,
                    String lap_specialization,
                    String the_state,
                    String lap_phone,
                    float lap_rate,
                    String lap_address,
                    String lap_location) {
        this.lap_id = lap_id;
        this.lap_name = lap_name;
        this.lap_image = lap_image;
        this.lap_specialization = lap_specialization;
        this.the_state = the_state;
        this.lap_phone = lap_phone;
        this.lap_rate=lap_rate;
        this.lap_address = lap_address;
        this.lap_location = lap_location;
    }

    public int getLap_id() {
        return lap_id;
    }
    public String getLap_name() {
        return lap_name;
    }
    public String getLap_image() {
        return lap_image;
    }
    public String getLap_specialization() {
        return lap_specialization;
    }
    public String getThe_state() {
        return the_state;
    }
    public String getLap_phone() {
        return lap_phone;
    }
    public String getLap_address() {
        return lap_address;
    }
    public String getLap_location() {
        return lap_location;
    }
    public float getLap_rate() {
        return  lap_rate;
    }
}