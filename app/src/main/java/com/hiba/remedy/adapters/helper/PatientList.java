package com.hiba.remedy.adapters.helper;

public class PatientList {

    private int queue_id;
    private String patient_name;
    private String symptoms;
    private String tests;
    private String results;
    private String diagnosis;
    private String medicine;
    private String pharmacist_note;
    private String diagnosis_date;



    private String last_update ;
    private int doctor_id;



    private String doctor_name;
    private int user_id;

    public int getQueue_id() {
        return queue_id;
    }

    public String getPatient_name() {
        return patient_name;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public String getTests() {
        return tests;
    }

    public String getResults() {
        return results;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public String getMedicine() {
        return medicine;
    }

    public String getPharmacist_note() {
        return pharmacist_note;
    }

    public int getDoctor_id() {
        return doctor_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getDiagnosis_date() { return diagnosis_date; }

    public String getLast_update() { return last_update; }

    public String getDoctor_name() {
        return doctor_name;
    }

    public PatientList(int queue_id,
                       String patient_name,
                       String symptoms,
                       String tests,
                       String results,
                       String diagnosis,
                       String medicine,
                       String pharmacist_note,
                       int doctor_id,
                       String doctor_name,
                       int user_id,
                       String diagnosis_date,
                       String last_update) {
        this.queue_id = queue_id;
        this.patient_name = patient_name;
        this.symptoms = symptoms;
        this.tests = tests;
        this.results = results;
        this.diagnosis = diagnosis;
        this.medicine = medicine;
        this.pharmacist_note = pharmacist_note;
        this.doctor_id = doctor_id;
        this.doctor_name = doctor_name;
        this.user_id = user_id;
        this.diagnosis_date = diagnosis_date;
        this.last_update = last_update;

    }


}
