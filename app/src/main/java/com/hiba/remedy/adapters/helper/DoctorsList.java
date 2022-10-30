package com.hiba.remedy.adapters.helper;

public class DoctorsList {
    private final float appointment_price;
    private final int doctor_id;
    private final String doctor_name;
    private final String doctor_image;
    private final String doctor_specialization;
    private final String the_state;
    private final String doctor_phone;
    private final String doctor_Workplace;


    public DoctorsList(int doctor_id,
                       String doctor_name,
                       String doctor_image,
                       String doctor_specialization,
                       String the_state,
                       String doctor_phone,
                       String doctor_Workplace,
                       float appointment_price) {
//        {"doctor_id":"1",
//        "doctor_name":"\u0645\u062d\u0645\u062f \u0627\u0644\u062d\u0633\u0646 \u0639\u0635\u064a\u062f\u0647 \u0628\u0644\u0628\u0646",
//        "doctor_image":"testImage2.jpg",
//        "doctor_specialization":"\u0627\u0647\u0635\u0627\u0626\u064a \u0643\u0646\u0627\u0641\u0629",
//        "doctor_phone":"926666565",
//        "doctor_Workplace":"\u0645\u0633\u062a\u0634\u0641\u0649 \u0627\u0644\u0639\u0628\u062f",
//        "appointment_price":"0"}
        this.doctor_id = doctor_id;
        this.doctor_name = doctor_name;
        this.doctor_image = doctor_image;
        this.doctor_specialization = doctor_specialization;
        this.the_state = the_state;
        this.doctor_phone = doctor_phone;
        this.doctor_Workplace=doctor_Workplace;
        this.appointment_price=appointment_price;

    }

    public int getDoctor_id() {
        return doctor_id;
    }
    public String getDoctor_name() {
        return doctor_name;
    }
    public String getDoctor_image() {
        return doctor_image;
    }
    public String getDoctor_specialization() {
        return doctor_specialization;
    }
    public String getThe_state() { return the_state; }
    public String getDoctor_phone() {
        return doctor_phone;
    }
    public String getDoctor_Workplace() {
        return doctor_Workplace;
    }
    public float getAppointment_price() {
        return appointment_price;
    }

}
