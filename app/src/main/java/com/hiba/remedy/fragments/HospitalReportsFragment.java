package com.hiba.remedy.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.hiba.remedy.R;
import com.hiba.remedy.URLs.URL;

import com.hiba.remedy.adapters.helper.DoctorsList;
import com.hiba.remedy.helpers.RemedyHelper;
import com.leavjenn.smoothdaterangepicker.date.SmoothDateRangePickerFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


@SuppressLint("ValidFragment")
public class HospitalReportsFragment extends Fragment{
    public BarEntry values ;
    public BarChart chart;
    Button b_submit;
    EditText et_from_date,et_to_date;
    Activity activity;
    RemedyHelper remedyHelper;
    ProgressDialog progressDialog;
    private ArrayList yAxis = null;
    private ArrayList yValues;
    private ArrayList xAxis1;
    private TextView appointment_num,patient_num,doct_num;
    URL url;
    Spinner sp_doctor_names;
    final Calendar myCalendar = Calendar.getInstance();
    ArrayList<DoctorsList> doctorsLists = new ArrayList<DoctorsList>();
    List<String> lables;
    LinearLayout LL_docList,LL_docNum;

    DatePickerDialog.OnDateSetListener from_date,to_date;

    public HospitalReportsFragment(Activity activity) {
        this.activity=activity;
        remedyHelper=new RemedyHelper(activity);
        progressDialog=new ProgressDialog(activity);
        lables=new ArrayList<String>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_hospital_reports, container, false);
        initLayout(view);
        return view;
    }

    private void initLayout(View view) {

//        chart          = (BarChart) view.findViewById(R.id.chart);
        b_submit         = view.findViewById(R.id.b_submit);
        et_from_date      = view.findViewById(R.id.et_from_date);
        sp_doctor_names   = view.findViewById(R.id.sp_doctor_names);
        appointment_num   = view.findViewById(R.id.appointment_num);
        doct_num          = view.findViewById(R.id.doct_num);
        patient_num      = view.findViewById(R.id.patient_num);
        et_to_date       = view.findViewById(R.id.et_to_date);
        LL_docList       = view.findViewById(R.id.LL_docList);
        LL_docNum       = view.findViewById(R.id.LL_docNum);

        LL_docList.setVisibility(View.VISIBLE);
        LL_docNum.setVisibility(View.VISIBLE);

        et_from_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(activity, from_date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

        et_to_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(activity, to_date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        from_date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateFrom();
            }

        };

        to_date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateTo();
            }

        };
        b_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String from= et_from_date.getText().toString().trim();
               String to= et_to_date.getText().toString().trim();
                final DoctorsList itemList;
    if(sp_doctor_names.getSelectedItemPosition()==0){
            itemList = doctorsLists.get(sp_doctor_names.getSelectedItemPosition());
         }      else{
             itemList = doctorsLists.get(sp_doctor_names.getSelectedItemPosition()-1);
         }
               remedyHelper.log("Selected doctor id", String.valueOf(itemList.getDoctor_id()));

               String doctor_id= String.valueOf(itemList.getDoctor_id());
               String doctor_name= sp_doctor_names.getSelectedItem().toString().trim();

               if(validation(from,to)){
                   remedyHelper.log("SELECTED_ITEM_ID", String.valueOf(sp_doctor_names.getSelectedItemId()));
                   if (sp_doctor_names.getSelectedItemId()==0){
                       if(remedyHelper.getValueOfKey("doctors_number").equals("")){
                           getSpceficTimeReport(remedyHelper.getValueOfKey("user_id"),from,to);
                       }else {
                           doct_num.setText(remedyHelper.getValueOfKey("doctors_number"));
                           getSpceficTimeReport(remedyHelper.getValueOfKey("user_id"),from,to);
                       }
                   }else {
                       getSpceficTimeReportForSpceficDoctor(remedyHelper.getValueOfKey("user_id"),from,to,doctor_name,doctor_id);
                   }
               }

            }
        });

//        filChart(getResources().getString(R.string.title_doctors),0,
//                 getResources().getString(R.string.patient_lable),0,
//                 getResources().getString(R.string.appointment)  ,0);
         getDoctorPatientAppointmentTotal(remedyHelper.getValueOfKey("user_id"));
        fillDoctorSpinner();

    }

    public Boolean validation(String from, String to  ){

        if (from.length() == 0 || to.length() == 0)
        {
            if (from.length() == 0) {
                et_from_date.setError(getResources().getString(R.string.type_from_date));
                Toast.makeText(activity,R.string.type_from_date, Toast.LENGTH_LONG).show();
                return false;
            }
            if (to.length() == 0) {
                et_to_date.setError(getResources().getString(R.string.type_to_date));
                Toast.makeText(activity,R.string.type_to_date, Toast.LENGTH_LONG).show();
                return false;
            }
            return false;
        }
        else {
            if (from.length() != 0) {
                if (to.length() ==10 ) {
                  return true;
                  } else {
                    et_to_date.setError(getResources().getString(R.string.type_to_date));
                    Toast.makeText(activity,R.string.type_to_date, Toast.LENGTH_LONG).show();
                    return false;
                  }
              }
              else{
                et_from_date.setError(getResources().getString(R.string.type_from_date));
                Toast.makeText(activity,R.string.type_from_date, Toast.LENGTH_LONG).show();
                return false;
            }
        }
    }

    private void filChart(String name1,int val1,String name2,int val2,String name3,int val3) {
        xAxis1 = new ArrayList<>();
        yAxis  =  new ArrayList<>();
        yValues = new ArrayList<>();

        xAxis1.add(name1);
        xAxis1.add(name2);
        xAxis1.add(name3);
        values = new BarEntry(val1,0);
        yValues.add(values);
        values = new BarEntry(val2,1);
        yValues.add(values);
        values = new BarEntry(val3,2);
        yValues.add(values);

        BarDataSet barDataSet1 = new BarDataSet(yValues,getResources().getString(R.string.one_person));
        barDataSet1.setColor(R.color.colorPrimaryLight);


        yAxis.add(barDataSet1);
        String[] names = (String[]) xAxis1.toArray(new String[xAxis1.size()]);
        BarData data = new BarData(names, yAxis);
        chart.setData(data);
        chart.setDescription(getResources().getString(R.string.hospital));
        chart.animateXY(5000, 5000);
        chart.invalidate();
    }

    public void getDoctorPatientAppointmentTotal(final String hospital_id){
        progressDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, URL.BASE_URL + URL.GET_TOTAL_REPORT_FOR_HOSPITAL_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject= new JSONObject(response);
                            remedyHelper.log("RESPONSE",response);

                            doct_num.setText(jsonObject.getString("doctors_number"));
                            remedyHelper.setValueToKey("doctors_number",jsonObject.getString("doctors_number"));
                            patient_num.setText(jsonObject.getString("patients_number"));
                            appointment_num.setText(jsonObject.getString("appointments_number"));
//                         filChart(getResources().getString(R.string.title_doctors),Integer.parseInt(jsonObject.getString("doctors_number")),
//                                  getResources().getString(R.string.patient_lable),Integer.parseInt(jsonObject.getString("patients_number")),
//                                  getResources().getString(R.string.appointment)  ,Integer.parseInt(jsonObject.getString("appointments_number")));
                                progressDialog.dismiss();
                                // clearForm();
//                                finish();

                        }catch (Exception e){
                            Log.d("Exception",e.toString());

                            progressDialog.dismiss();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),R.string.no_internet_connection, Toast.LENGTH_LONG).show();
                Snackbar.make(getView(),R.string.no_internet_connection, Snackbar.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("hospital_id",hospital_id);
                return parameters;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(request);
    }

    public void getSpceficTimeReport(final String hospital_id, final String form, final String to){
        progressDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, URL.BASE_URL + URL.GET_TIME_REPORT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject= new JSONObject(response);
                            remedyHelper.log("RESPONSE",response);

                            patient_num.setText(jsonObject.getString("patients_number"));
                            appointment_num.setText(jsonObject.getString("appointments_number"));

                            et_from_date.setText("");
                            et_to_date.setText("");
                            sp_doctor_names.setSelection(0);
//                            filChart(getResources().getString(R.string.title_doctors),0,
//                                    getResources().getString(R.string.patient_lable),Integer.parseInt(jsonObject.getString("patients_number")),
//                                    getResources().getString(R.string.appointment)  ,Integer.parseInt(jsonObject.getString("appointments_number")));
                            progressDialog.dismiss();
                            // clearForm();
//                                finish();

                        }catch (Exception e){
                            Log.d("Exception",e.toString());

                            progressDialog.dismiss();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),R.string.no_internet_connection, Toast.LENGTH_LONG).show();
                Snackbar.make(getView(),R.string.no_internet_connection, Snackbar.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("hospital_id",hospital_id);
                parameters.put("from",form);
                parameters.put("to",to);
                return parameters;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(request);
    }

    public void getSpceficTimeReportForSpceficDoctor(final String hospital_id, final String from, final String to, final String doctor_name, final String doctor_id){
        progressDialog.show();
        remedyHelper.log("RESPONSE_IN","begin");
        StringRequest request = new StringRequest(Request.Method.POST, URL.BASE_URL + URL.GET_TIME_REPORT_FOR_SPCIFIC_DOCTOR_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            remedyHelper.log("RESPONSE_IN_spcific_doc",response);
                            JSONObject jsonObject= new JSONObject(response);
                            doct_num.setText(doctor_name);
                            patient_num.setText(jsonObject.getString("patients_number"));
                            appointment_num.setText(jsonObject.getString("appointments_number"));
                            et_from_date.setText("");
                            et_to_date.setText("");
                            sp_doctor_names.setSelection(0);
//                            filChart(getResources().getString(R.string.title_doctors),0,
//                                    getResources().getString(R.string.patient_lable),Integer.parseInt(jsonObject.getString("patients_number")),
//                                    getResources().getString(R.string.appointment)  ,Integer.parseInt(jsonObject.getString("appointments_number")));
                            progressDialog.dismiss();
                            // clearForm();
//                                finish();

                        }catch (Exception e){
                            remedyHelper.log("Exception_SRFSD",e.toString());
                            progressDialog.dismiss();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),R.string.no_internet_connection, Toast.LENGTH_LONG).show();
                Snackbar.make(getView(),R.string.no_internet_connection, Snackbar.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();

                remedyHelper.log("hospital_id",hospital_id);
                remedyHelper.log("from",from);
                remedyHelper.log("to",to);
                remedyHelper.log("doctor_id",doctor_id);

                parameters.put("hospital_id",hospital_id);
                parameters.put("from",from);
                parameters.put("to",to);
                parameters.put("doctor_id",doctor_id);
                return parameters;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(request);
    }
    public void fillDoctorSpinner() {
        progressDialog.dismiss();
        doctorsLists.clear();
        StringRequest stringRequest =new StringRequest(Request.Method.POST,
                URL.BASE_URL+URL.DISPLAY_DOCTORS_HOSPITAL_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            Log.d("Response", s);
                            JSONObject jsonObject= new JSONObject(s);
                            JSONArray array = jsonObject.getJSONArray("doctors_table");
                            for(int i=0;i<array.length();i++){
                                JSONObject o = array.getJSONObject(i);
                                DoctorsList item= new DoctorsList(
                                        o.getInt("doctor_id"),
                                        o.getString("doctor_name"),
                                        o.getString(  "doctor_image"),
                                        o.getString("doctor_specialization"),
                                        o.getString("the_state"),
                                        o.getString("doctor_phone"),
                                        o.getString("doctor_Workplace"),
                                        o.getLong("appointment_price"));

                                doctorsLists.add(item);
                            }

                            lables.add(getResources().getString(R.string.doctors_list_sp));

                            for (int i = 0; i < doctorsLists.size(); i++) {
                                lables.add(doctorsLists.get(i).getDoctor_name());
                            }
                            // Creating adapter for spinner
                            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(activity,
                                    android.R.layout.simple_spinner_item, lables);

                            // Drop down layout style - list view with radio button
                            spinnerAdapter
                                    .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                            // attaching data adapter to spinner
                            sp_doctor_names.setAdapter(spinnerAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("JSONException", e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(activity,R.string.connection_failed,Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();

                parameters.put("user_id",remedyHelper.getValueOfKey("user_id"));

                return parameters;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
        progressDialog.dismiss();
    }


    private void updateFrom() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        et_from_date.setText(sdf.format(myCalendar.getTime()));
    }
    private void updateTo() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        et_to_date.setText(sdf.format(myCalendar.getTime()));
    }
}
