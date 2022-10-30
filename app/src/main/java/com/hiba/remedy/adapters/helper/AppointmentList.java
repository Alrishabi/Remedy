package com.hiba.remedy.adapters.helper;

public class AppointmentList {
    private final String user_phone;
    private final int appointment_id;
    private final float appointment_price;
    private final int doctor_id;
    private final String appointment_address;
    private final String doctor_name;
    private final String appointment_date;
    private final String patient_name;
    private final int user_id;
    private final String doctor_phone;

    public String getHospital_id() {
        return hospital_id;
    }

    private final String hospital_id;
//    {"appointment_id":"16"
//    ,"patient_name":""
//    ,"appointment_price":"0"
//    ,"doctor_id":"0"
//    ,"doctor_name":""
//    ,"appointment_address":""
//    ,"appointment_date":""
//    ,"user_id":"0"}]}

    public AppointmentList(int appointment_id,
                           String patient_name,
                           String user_phone,
                           float appointment_price,
                           int doctor_id,
                           String doctor_name,
                           String doctor_phone,
                           String appointment_address,
                           String appointment_date,
                           int user_id,
                           String hospital_id)
    {
//                              {"appointment_id":"4",
//                                "patient_name":"",
//                                "user_phone":"",
//                                "appointment_price":"0",
//                                "doctor_id":"0",
//                                "doctor_name":"",
//                                "doctor_phone":"",
//                                "appointment_address":"",
//                                "appointment_date":"",
//                                "user_id":"0"}
        this.appointment_id = appointment_id;
        this.appointment_price = appointment_price;
        this.doctor_id = doctor_id;
        this.user_phone = user_phone;
        this.doctor_name = doctor_name;
        this.doctor_phone = doctor_phone;
        this.appointment_address = appointment_address;
        this.appointment_date = appointment_date;
        this.patient_name=patient_name;
        this.user_id = user_id;
        this.hospital_id = hospital_id;
    }

    public int getAppointment_id() {
        return appointment_id;
    }
    public float getAppointment_price() {
        return appointment_price;
    }
    public String getDoctor_name() {
        return doctor_name;
    }
    public String getAppointment_address() {
        return appointment_address;
    }
    public String getAppointment_date() {
        return appointment_date; }

    public String getPatient_name() {
        return patient_name;
    }
    public int getUser_id() {
        return user_id;
    }

    public String getDoctor_phone() {
        return doctor_phone;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public int getDoctor_id() {
        return doctor_id;
    }
}
