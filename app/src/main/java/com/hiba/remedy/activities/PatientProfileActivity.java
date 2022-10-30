package com.hiba.remedy.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.widget.TextView;

import com.hiba.remedy.R;
import com.hiba.remedy.adapters.helper.PatientAdapter;
import com.hiba.remedy.adapters.helper.PatientList;
import com.hiba.remedy.helpers.RemedyHelper;

import java.util.ArrayList;

public class PatientProfileActivity extends AppCompatActivity {
    private RecyclerView recyclerPharmacy;
    private ArrayList<PatientList> patientLists = new ArrayList<PatientList>();
    PatientAdapter mAdapter;
    RemedyHelper remedyHelper;
    Activity activity;
    PatientList patientList;
    SearchView.OnQueryTextListener b;
    SwipeRefreshLayout swipeRefreshLayout;
    ProgressDialog progressDialog;
    TextView  tv_patient ,
    tv_queue_id ,
    tv_symptoms,
    tv_tests ,
    tv_results,
    tv_diagnosis,
    tv_medicine,
    tv_pharmacist,
    tv_doctor_id ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_profile);
        initContext();
        initLayout();
    }

    public void initContext(){
        activity = this;
        this.remedyHelper=new RemedyHelper(activity);
        progressDialog = new ProgressDialog(activity);
    }
    public void initLayout(){
         setTitle(R.string.patient_info);
        tv_patient =   (TextView)findViewById(R.id.tv_patient);
        tv_queue_id = (TextView)findViewById(R.id.tv_queue_id);
        tv_symptoms = (TextView)findViewById(R.id.tv_symptoms);
        tv_tests   =  (TextView)findViewById(R.id.tv_tests);
        tv_results = (TextView)findViewById(R.id.tv_results);
        tv_diagnosis = (TextView)findViewById(R.id.tv_diagnosis);
        tv_medicine = (TextView)findViewById(R.id.tv_medicine);
        tv_pharmacist = (TextView)findViewById(R.id.tv_pharmacist);
        tv_doctor_id = (TextView)findViewById(R.id.tv_doctor_id);

        final String  patient_name = getIntent().getExtras().getString("PATIENT_KEY");
        final int  queue_id = getIntent().getExtras().getInt("QUEUE_ID_KEY");
        final String  symptoms = getIntent().getExtras().getString("SYMPTOMS_KEY");
        final String  tests = getIntent().getExtras().getString("TESTS_KEY");
        final String  results = getIntent().getExtras().getString("RESULTS_KEY");
        final String  diagnosis = getIntent().getExtras().getString("DIAGNOSIS_KEY");
        final String  medicine = getIntent().getExtras().getString("MEDICINE_KEY");
        final String  pharmacist = getIntent().getExtras().getString("PHARMACIST_KEY");
        final int  doctor_id = getIntent().getExtras().getInt("DOCTOR_ID_KEY");

        tv_patient.setText(patient_name);
        remedyHelper.log("queue_id in patient profile",String.valueOf(queue_id));
        tv_queue_id.setText(String.valueOf(queue_id));
        tv_symptoms.setText(symptoms);
        tv_tests.setText(tests);
        tv_results.setText(results);
        tv_diagnosis.setText(diagnosis);
        tv_medicine.setText(medicine);
        tv_pharmacist.setText(pharmacist);
        tv_doctor_id.setText(String.valueOf(doctor_id));
    }

    private void showPatientInfo(){

    }
}
