package com.hiba.remedy.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
import com.hiba.remedy.helpers.RemedyHelper;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


@SuppressLint("ValidFragment")
public class DoctorReportsFragment extends Fragment {
    public BarEntry values ;
    public BarChart chart;
    private Button b_submit;
    private EditText et_from_date,et_to_date;
    private Activity activity;
    private RemedyHelper remedyHelper;
    private ProgressDialog progressDialog;
    private ArrayList yAxis = null;
    private ArrayList yValues;
    private ArrayList xAxis1;
    private TextView appointment_num,patient_num;
    final Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener from_date,to_date;


    public DoctorReportsFragment(Activity activity) {
        this.activity=activity;
        remedyHelper=new RemedyHelper(activity);
        progressDialog=new ProgressDialog(activity);
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
//        chart        = (BarChart) view.findViewById(R.id.chart);
        b_submit     = (Button) view.findViewById(R.id.b_submit);
        et_from_date = (EditText) view.findViewById(R.id.et_from_date);
        et_to_date   = (EditText) view.findViewById(R.id.et_to_date);
        appointment_num   = (TextView) view.findViewById(R.id.appointment_num);
         patient_num      = (TextView) view.findViewById(R.id.patient_num);


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
                getSpceficTimeReport(remedyHelper.getValueOfKey("user_id"),from,to);
            }
        });

//        filChart(getResources().getString(R.string.patient_lable),0,
//                 getResources().getString(R.string.appointment) ,0);
        getPatientAppointmentTotal(remedyHelper.getValueOfKey("user_id"));
    }
    private void filChart(String name2,int val2,String name3,int val3) {
        xAxis1 = new ArrayList<>();
        yAxis  =  new ArrayList<>();
        yValues = new ArrayList<>();

        xAxis1.add(name2);
        xAxis1.add(name3);

        values = new BarEntry(val2,0);
        yValues.add(values);
        values = new BarEntry(val3,1);
        yValues.add(values);

        BarDataSet barDataSet1 = new BarDataSet(yValues,getResources().getString(R.string.one_person));
        barDataSet1.setColor(R.color.colorPrimaryLight);


        yAxis.add(barDataSet1);
        String names[]= (String[]) xAxis1.toArray(new String[xAxis1.size()]);
        BarData data = new BarData(names, yAxis);
        chart.setData(data);
        chart.setDescription(getResources().getString(R.string.doctor));
        chart.animateXY(5000, 5000);
        chart.invalidate();
    }
    public void getPatientAppointmentTotal(final String doctor_id){
        progressDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, URL.BASE_URL + URL.GET_TOTAL_REPORT_FOR_DOCTORS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject= new JSONObject(response);
                            remedyHelper.log("RESPONSE_IN_DOCTOR_REPORT_in_spsific_date",response);
                            patient_num.setText(jsonObject.getString("patients_number"));
                            appointment_num.setText(jsonObject.getString("appointments_number"));
//                            Toast.makeText(getContext(),R.string.report_by, Toast.LENGTH_LONG).show();

//                            filChart(getResources().getString(R.string.patient_lable),Integer.parseInt(jsonObject.getString("patients_number")),
//                                    getResources().getString(R.string.appointment)  ,Integer.parseInt(jsonObject.getString("appointments_number")));
                            progressDialog.dismiss();
                        }catch (Exception e){
                            Log.d("EXCEPTION",e.toString());
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
                parameters.put("doctor_id",doctor_id);
                return parameters;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(request);
    }
    public void getSpceficTimeReport(final String hospital_id, final String form, final String to){
        progressDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST,URL.BASE_URL+URL.GET_TIME_REPORT_FOR_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject= new JSONObject(response);
                            remedyHelper.log("RESPONSE_DOCTOR_REPORT",response);

                            patient_num.setText(jsonObject.getString("patients_number"));
                            appointment_num.setText(jsonObject.getString("appointments_number"));
                            Toast.makeText(getContext(),R.string.report_by_date, Toast.LENGTH_LONG).show();

                            et_from_date.setText("");
                              et_to_date.setText("");
//                            filChart(getResources().getString(R.string.patient_lable),Integer.parseInt(jsonObject.getString("patients_number")),
//                                    getResources().getString(R.string.appointment)  ,Integer.parseInt(jsonObject.getString("appointments_number")));
                            progressDialog.dismiss();
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
