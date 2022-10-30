package com.hiba.remedy.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hiba.remedy.R;
import com.hiba.remedy.URLs.URL;
import com.hiba.remedy.adapters.helper.AppointmentAdapter;
import com.hiba.remedy.adapters.helper.AppointmentList;
import com.hiba.remedy.helpers.RemedyHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@SuppressLint("ValidFragment")
public class TakePatientFragment extends Fragment {

    AppointmentAdapter mAdapter;
    Activity activity;
    RemedyHelper remedyHelper;
    ProgressDialog progressDialog;
    EditText et_queue_id,et_patient_name,et_symptoms,et_tests,et_results,et_diagnosis,et_medicine,et_pharmacist_note;
    Button bt_check_patient,bt_update_patient;
    RelativeLayout rl_queue_id , rl_patient_name , rl_symptoms , rl_tests  , rl_results , rl_diagnosis, rl_medicine, rl_pharmacist_note;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =inflater.inflate(R.layout.fragment_take_patient, container, false);

        initiateLayout(rootView);
        return rootView;
    }
    @SuppressLint("ValidFragment")
    public TakePatientFragment(Activity activity) {
        this.activity=activity;
        remedyHelper=new RemedyHelper(activity);
        progressDialog =new ProgressDialog(activity);
    }

    public  void initiateLayout(View view){
        setHasOptionsMenu(true);
        progressDialog.setMessage(getResources().getString(R.string.loading_progress_dialog_message));

        rl_queue_id =  view.findViewById(R.id.rl_queue_id);
        rl_patient_name =  view.findViewById(R.id.rl_patient_name);
        rl_symptoms =  view.findViewById(R.id.rl_symptoms);
        rl_tests =  view.findViewById(R.id.rl_tests);
        rl_results =  view.findViewById(R.id.rl_results);
        rl_diagnosis =  view.findViewById(R.id.rl_diagnosis);
        rl_medicine =  view.findViewById(R.id.rl_medicine);
        rl_pharmacist_note =  view.findViewById(R.id.rl_pharmacist_note);



        et_queue_id     =  view.findViewById(R.id.et_queue_id);
        et_patient_name =  view.findViewById(R.id.et_patient_name);
        et_symptoms     =  view.findViewById(R.id.et_symptoms);
        et_tests        =  view.findViewById(R.id.et_tests);
        et_results      =  view.findViewById(R.id.et_results);
        et_diagnosis    =  view.findViewById(R.id.et_diagnosis);
        et_medicine     =  view.findViewById(R.id.et_medicine);
        et_pharmacist_note =  view.findViewById(R.id.et_pharmacist_note);

        if (remedyHelper.getValueOfKey("user_category").equals("3")){
            et_patient_name.setEnabled(false);
            et_results.setEnabled(false);
            et_pharmacist_note.setEnabled(false);
        }else if(remedyHelper.getValueOfKey("user_category").equals("4")){
            et_patient_name.setEnabled(false);
            et_symptoms.setEnabled(false);
            et_tests.setEnabled(false);
            et_diagnosis.setEnabled(false);
            et_medicine.setEnabled(false);
            et_pharmacist_note.setEnabled(false);
        }
        else{
            et_patient_name.setEnabled(false);
            et_symptoms.setEnabled(false);
            et_tests.setEnabled(false);
            et_diagnosis.setEnabled(false);
            et_medicine.setEnabled(false);
            et_results.setEnabled(false);
        }

         bt_check_patient =  view.findViewById(R.id.b_diagnosis);
         bt_update_patient =  view.findViewById(R.id.b_submit);

         bt_check_patient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String queue_id=et_queue_id.getText().toString().trim();
                checkQueueID(queue_id);
            }
        });

        bt_update_patient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String queue_id=et_queue_id.getText().toString().trim();
                String patient=et_patient_name.getText().toString().trim();
                String symptoms=et_symptoms.getText().toString().trim();
                String tests=et_tests.getText().toString().trim();
                String results=et_results.getText().toString().trim();
                String diagnosis=et_diagnosis.getText().toString().trim();
                String medicine=et_medicine.getText().toString().trim();
                String pharmacist_note=et_pharmacist_note.getText().toString().trim();

                upDatePatientInfo(queue_id,patient,symptoms,tests,results,diagnosis,medicine,pharmacist_note);
            }
        });

    }
    public void checkQueueID(final String queue_id) {
        progressDialog.show();
        StringRequest stringRequest =new StringRequest(Request.Method.POST,
                URL.BASE_URL+URL.CHECK_QUEUE_ID_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            Log.d("Response", s);
                            JSONObject jsonObject= new JSONObject(s);
                            if(jsonObject.getString("code").equals("0")){
                                if(remedyHelper.getValueOfKey("app_language").equals("en")) {
                                    Toast.makeText(activity, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                                }else {
                                    Toast.makeText(activity, jsonObject.getString("message_ar"), Toast.LENGTH_LONG).show();
                                }                                remedyHelper.setValueToKey("Queue_id",jsonObject.getString("queue_id"));



                                rl_queue_id.setVisibility(View.VISIBLE);
                                rl_patient_name.setVisibility(View.VISIBLE);
                                rl_symptoms.setVisibility(View.VISIBLE);
                                rl_tests.setVisibility(View.VISIBLE);
                                rl_results.setVisibility(View.VISIBLE);
                                rl_diagnosis.setVisibility(View.VISIBLE);
                                rl_medicine.setVisibility(View.VISIBLE);
                                rl_pharmacist_note.setVisibility(View.VISIBLE);
                                bt_update_patient.setVisibility(View.VISIBLE);
                                bt_check_patient.setVisibility(View.GONE);


                                et_queue_id.setText(jsonObject.getString("queue_id"));
                                et_patient_name.setText(jsonObject.getString("patient_name"));
                                et_symptoms.setText (jsonObject.getString("symptoms"));
                                et_tests.setText    (jsonObject.getString("tests"));
                                et_results.setText  (jsonObject.getString("results"));
                                et_diagnosis.setText(jsonObject.getString("diagnosis"));
                                et_medicine.setText  (jsonObject.getString("medicine"));
                                et_pharmacist_note.setText(jsonObject.getString("pharmacist_note"));
                                remedyHelper.setValueToKey("doctor_id",jsonObject.getString("doctor_id"));
                            }else {
                                if(remedyHelper.getValueOfKey("app_language").equals("en")) {
                                    Toast.makeText(activity, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                                }else {
                                    Toast.makeText(activity, jsonObject.getString("message_ar"), Toast.LENGTH_LONG).show();
                                }
                                Toast.makeText(activity,jsonObject.getString("error"),Toast.LENGTH_LONG).show();
                            }
                            progressDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("JSONExceptionA", e.toString());
                            progressDialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(),R.string.connection_failed,Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();

                parameters.put("queue_id",queue_id);
//                parameters.put("user_id","1");

                return parameters;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    public void upDatePatientInfo(
            final String queue_id,
            final String patient,
            final String symptoms,
            final String tests,
            final String results,
            final String diagnosis,
            final String medicine,
            final String pharmacist_note) {

        remedyHelper.log("queue_id",queue_id);
        remedyHelper.log("patient",patient);
        remedyHelper.log("symptoms",symptoms);
        remedyHelper.log("tests",tests);
        remedyHelper.log("results",results);
        remedyHelper.log("diagnosis",diagnosis);
        remedyHelper.log("medicine",medicine);
        remedyHelper.log("pharmacist_note",pharmacist_note);
        remedyHelper.log("doctor_id",remedyHelper.getValueOfKey("user_id"));

        progressDialog.show();
        StringRequest stringRequest =new StringRequest(Request.Method.POST,
                URL.BASE_URL+URL.UPDATE_PATIENT_INFO_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            Log.d("Response", s);
                            JSONObject jsonObject= new JSONObject(s);
                            if(jsonObject.getString("code").equals("0")){
                                if(remedyHelper.getValueOfKey("app_language").equals("en")) {
                                    Toast.makeText(activity, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                                }else {
                                    Toast.makeText(activity, jsonObject.getString("message_ar"), Toast.LENGTH_LONG).show();
                                }
                                rl_queue_id.setVisibility(View.VISIBLE);
                                rl_patient_name.setVisibility(View.GONE);
                                rl_symptoms.setVisibility(View.GONE);
                                rl_tests.setVisibility(View.GONE);
                                rl_results.setVisibility(View.GONE);
                                rl_diagnosis.setVisibility(View.GONE);
                                rl_medicine.setVisibility(View.GONE);
                                rl_pharmacist_note.setVisibility(View.GONE);

                                bt_update_patient.setVisibility(View.GONE);
                                bt_check_patient.setVisibility(View.VISIBLE);
                                et_queue_id.setText("");

                            }else {
                                Toast.makeText(activity,jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                                Toast.makeText(activity,jsonObject.getString("error"),Toast.LENGTH_LONG).show();
                            }

                            progressDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("JSONExceptionA", e.toString());
                            progressDialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(),R.string.connection_failed,Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();

                parameters.put("queue_id",queue_id);
                parameters.put("patient_name",patient);
                parameters.put("symptoms",symptoms);
                parameters.put("tests",tests);
                parameters.put("results",results);
                parameters.put("diagnosis",diagnosis);
                parameters.put("medicine",medicine);
                parameters.put("pharmacist_note",pharmacist_note);
                parameters.put("doctor_id",remedyHelper.getValueOfKey("user_id"));

                return parameters;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }

}
