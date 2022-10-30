package com.hiba.remedy.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.hiba.remedy.R;
import com.hiba.remedy.URLs.URL;
import com.hiba.remedy.helpers.RemedyHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class GetAnAppointmentActivity extends AppCompatActivity {

    EditText et_patient_name,et_phone_number,et_date,et_answer_qu,et_id;
    Button bt_appoint;
    TextView doc_phone,doc_name,ed_price,et_appo_address;
    LinearLayout mRoot;
    Activity activity;
    RemedyHelper remedyHelper;
    ProgressDialog progressDialog;
    public float    appointment_price1;
    final Calendar myCalendar = Calendar.getInstance();
    List<String> periodList;
    Spinner sp_period;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_an_appointment);

         initiateContext();

         initiateLayout();
    }

    private void initiateContext(){
        activity        = this;
        remedyHelper          = new RemedyHelper(activity);
        progressDialog = new ProgressDialog(activity);
        periodList = new ArrayList<String>();
    }

    public  void initiateLayout(){

        et_patient_name           = findViewById(R.id.et_patient_name);
        et_phone_number           = findViewById(R.id.et_phone_number);
        et_date                   = findViewById(R.id.et_date);
        doc_name                  = findViewById(R.id.doc_name);
        doc_phone                 = findViewById(R.id.doc_phone);
        ed_price                  = findViewById(R.id.ed_price);
        et_appo_address           = findViewById(R.id.et_appo_address);
        bt_appoint                = findViewById(R.id.btn_appointment);
        mRoot                     = findViewById(R.id.linearL);
        sp_period                 = findViewById(R.id.sp_period);

        setTitle(getString(R.string.get_appointment));

        progressDialog.setMessage(getString(R.string.get_appointment)+"...");

   //   Receive DATA

        periodList.add(getResources().getString(R.string.period));
        periodList.add(getResources().getString(R.string.am));
        periodList.add(getResources().getString(R.string.pm));

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item,periodList);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(R.layout.spinner_item);
        // attaching data adapter to spinner
        sp_period.setAdapter(dataAdapter);

        final String   patient                  = remedyHelper.getValueOfKey("user_full_name");
        final String   user_phone               = remedyHelper.getValueOfKey("user_phone");
        appointment_price1                      = getIntent().getExtras().getFloat("PRICE_KEY");
        final int      doctor_id                = getIntent().getExtras().getInt("ID_KEY");
        final String   doctor_name              = getIntent().getExtras().getString("NAME_KEY");
        final String   doctor_phone             = getIntent().getExtras().getString("PHONE_KEY");
        final String   appointment_address      = getIntent().getExtras().getString("ADDRESS_KEY");
        final String   user_id                  = remedyHelper.getValueOfKey("user_id");

        et_patient_name.setText(patient);
        et_phone_number.setText(user_phone);
        et_date.setText("");
        doc_name.setText(doctor_name);
        doc_phone.setText(doctor_phone);
        ed_price.setText(String.valueOf(appointment_price1));
        et_appo_address.setText(appointment_address);

         final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        et_date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(GetAnAppointmentActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        bt_appoint.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {

            String fullname     =et_patient_name.getText().toString().trim();
            String pation_phone =et_phone_number.getText().toString().trim();
            String date =et_date.getText().toString().trim();
            long period =sp_period.getSelectedItemId();

                try {
                    if(validation(fullname,pation_phone,date,period)) {

                        Log.d("FULLNAME",fullname);
                        Log.d("PATIENTNAME",pation_phone);
                        Log.d("DATE",date);

                        final String uuid = UUID.randomUUID().toString();
                        remedyHelper.log("UUID",uuid);

                        insertMethod(activity, et_patient_name.getText().toString(), et_phone_number.getText().toString(),
                                String.valueOf(appointment_price1),
                                String.valueOf(doctor_id), doctor_name, doctor_phone,
                                appointment_address, et_date.getText().toString(), user_id,period);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Boolean validation(String fullName, String phone_num, String date,long period) throws ParseException {

//        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        LocalDateTime now = LocalDateTime.now();

        Date date1=new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(date);
        remedyHelper.log("Enter it date",date);
        remedyHelper.log("CURRENT DATE",Calendar.getInstance().getTime().toString());

        if (fullName.length() == 0 || phone_num.length() < 10 || date.length() == 0 ||date1.compareTo(Calendar.getInstance().getTime()) < 0 || period == 0)
        {
            if (fullName.length() == 0) {
                et_patient_name.setError(getResources().getString(R.string.type_patient_name));
                Toast.makeText(activity,R.string.type_patient_name, Toast.LENGTH_LONG).show();
                return false;
            }
            if (phone_num.length() < 10) {
                et_phone_number.setError(getResources().getString(R.string.type_patient_phone));
                Toast.makeText(activity,R.string.type_patient_phone, Toast.LENGTH_LONG).show();
                return false;
            }
            if (date.length() == 0) {
                et_date.setError(getResources().getString(R.string.type_date));
                Toast.makeText(activity,R.string.type_date, Toast.LENGTH_LONG).show();
                return false;
            }
            if (date1.compareTo(Calendar.getInstance().getTime()) < 0) {
//                et_date.setError(getResources().getString(R.string.type_date));
                Toast.makeText(activity,R.string.old_date, Toast.LENGTH_LONG).show();
                return false;
            }
            if (period==0) {
//                et_date.setError(getResources().getString(R.string.type_date));
                Toast.makeText(activity,R.string.choose_period, Toast.LENGTH_LONG).show();
                return false;
            }
            return false;
        }
        else {
            if (fullName.length() != 0) {
                if (phone_num.length() ==10 ) {
                    if (date.length() != 0) {
                        if (date1.compareTo(Calendar.getInstance().getTime()) > 0) {
                            if (period!=0) {
                                return true;
                            } else {
//                            et_date.setError(getResources().getString(R.string.type_password));
                                Toast.makeText(activity,R.string.choose_period, Toast.LENGTH_LONG).show();
                                return false;
                            }
                             } else {
//                            et_date.setError(getResources().getString(R.string.type_password));
                             Toast.makeText(activity,R.string.old_date, Toast.LENGTH_LONG).show();
                            return false;
                           }
                    } else {
                        et_date.setError(getResources().getString(R.string.type_password));
                        Toast.makeText(activity,R.string.type_date, Toast.LENGTH_LONG).show();

                        return false;
                    }
                } else {
                    et_phone_number.setError(getResources().getString(R.string.type_patient_phone));
                    Toast.makeText(activity,R.string.type_patient_phone, Toast.LENGTH_LONG).show();
                    return false;
                }
            }
            else{
                et_patient_name.setError(getResources().getString(R.string.type_patient_name));
                Toast.makeText(activity,R.string.type_patient_name, Toast.LENGTH_LONG).show();
                return false;
            }
        }
    }

    public String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        String strDate = mdformat.format(calendar.getTime());
//        display(strDate);
        return strDate;
    }

    public void clearForm(){
        et_id.setText("");
        et_patient_name.setText("");
        et_answer_qu.setText("");
        et_date.setText("");
        et_phone_number.setText("");

    }

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        et_date.setText(sdf.format(myCalendar.getTime()));
    }

    public void insertMethod(final Activity activityl,
                             final String patient_name,
                             final String user_phone,
                             final String appointment_price,
                             final String doctor_id,
                             final String doctor_name,
                             final String doctor_phone,
                             final String appointment_address,
                             final String appointment_date,
                             final String user_id,
                             final long period
                             ){
        progressDialog.show();

        Log.d("doctor_id",doctor_id);
        Log.d("doctor_name",doctor_name);
        try {
            Log.d("Hospital_ID_KEY",remedyHelper.getValueOfKey("Hospital_ID_KEY"));
        }catch (NullPointerException e){
            e.printStackTrace();
        }

        Log.d("appointment_date",appointment_date);
        Log.d("patient_name",patient_name);
        Log.d("user_id",user_id);
        Log.d("appointment_price",appointment_price);
            StringRequest request = new StringRequest(Request.Method.POST, URL.BASE_URL + URL.INSERT_APPOINTMENT_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("response",response);

                            try {
                                final JSONObject jsonObject= new JSONObject(response);
                                Log.d("jsonObject",jsonObject.toString());
                                if(jsonObject.getString("code").equals("0")){
                                    if(remedyHelper.getValueOfKey("app_language").equals("en")) {
                                        Toast.makeText(activityl, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                                    }else {
                                        Toast.makeText(activityl, jsonObject.getString("message_ar"), Toast.LENGTH_LONG).show();
                                    }
//                                    new AlertDialog.Builder(activity)
//                                        .setIcon(R.drawable.ic_attention)
//                                            .setTitle(R.string.alert_pay_title)
//                                            .setMessage(activity.getResources().getString(R.string.payment_qution))
//                                            .setPositiveButton(activity.getResources().getString(R.string.yes),new DialogInterface.OnClickListener() {
//                                                @Override
//                                                public void onClick(DialogInterface dialog, int which) {
//
//                                                    try {
//                                                        Log.d("APPOINTMENT_ID_insert", jsonObject.getString("appointment_id"));
//                                                        goToPaymentActivity(jsonObject.getString("appointment_id"), appointment_price1);
//                                                    } catch (JSONException e) {
//                                                        e.printStackTrace();
//                                                    }
//                                                    finish();
//                                                }
//                                            }).setNegativeButton(R.string.later_button,new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            finish();
//                                        }
//                                    })
//                                            .show();
                                    new AlertDialog.Builder(activity)
                                            .setIcon(R.drawable.ic_attention)
                                            .setTitle(R.string.queue_id)
                                            .setMessage(activity.getResources().getString(R.string.attion_queue_id_message)+jsonObject.getString("appointment_id"))
                                            .setPositiveButton(activity.getResources().getString(R.string.ok),new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    try {
                                                        Log.d("APPOINTMENT_ID_insert", jsonObject.getString("appointment_id"));
                                                        goToPaymentActivity(jsonObject.getString("appointment_id"), appointment_price1,appointment_date,doctor_id);
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                    finish();
                                                }
                                            })
                                            .show();
                                }else {
                                    if(remedyHelper.getValueOfKey("app_language").equals("en")) {
                                        Toast.makeText(activityl,jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                                        Toast.makeText(activityl,jsonObject.getString("error"), Toast.LENGTH_LONG).show();
                                    }else {
                                        Toast.makeText(activityl, jsonObject.getString("message_ar"), Toast.LENGTH_LONG).show();
                                        Toast.makeText(activityl,jsonObject.getString("error"), Toast.LENGTH_LONG).show();
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();

                                Log.d("JSONException",e.toString());
                                Toast.makeText(activityl,e.toString(),Toast.LENGTH_LONG).show();
                                progressDialog.dismiss();
                            }
                            progressDialog.dismiss();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                     Toast.makeText(activityl,R.string.insert_failed, Toast.LENGTH_LONG).show();
                    Snackbar.make(mRoot, R.string.no_internet_connection,Snackbar.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> parameters = new HashMap<String, String>();

                    Log.d("patient_name",patient_name);
                    Log.d("user_phone",user_phone);
                    Log.d("appointment_price","0");
                    Log.d("doctor_id",doctor_id);
                    Log.d("hospital_id",remedyHelper.getValueOfKey("Hospital_ID_KEY"));
                    Log.d("doctor_name",doctor_name);
                    Log.d("doctor_phone",doctor_phone);
                    Log.d("appointment_address",appointment_address);
                    Log.d("appointment_date",appointment_date);
                    Log.d("period", String.valueOf(period));
                    Log.d("user_id",user_id);
                    
                    parameters.put("patient_name",patient_name);
                    parameters.put("user_phone",user_phone);
                    parameters.put("appointment_price",appointment_price);
                    parameters.put("doctor_id",doctor_id);

                    try {
                        parameters.put("hospital_id",remedyHelper.getValueOfKey("Hospital_ID_KEY"));
                    }catch (NullPointerException e){
                        e.printStackTrace();
                    }
                    parameters.put("doctor_name",doctor_name);
                    parameters.put("doctor_phone",doctor_phone);
                    parameters.put("appointment_address",appointment_address);
                    parameters.put("appointment_date",appointment_date);
                    parameters.put("user_id",user_id);
                    parameters.put("period",String.valueOf(period));

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
                    return parameters;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(activityl);
            requestQueue.add(request);
    }

    public void goToPaymentActivity(  final String appointment_id,
                                      final float appointment_price,
                                      final String appointment_date,
                                      final String doctor_id){

//        final String patient_name,
//        final String user_phone,
//        final String appointment_price,
//        final String doctor_id,
//        final String doctor_name,
//        final String doctor_phone,
//        final String appointment_address,
//        final String appointment_date,
//        final String user_id

        Bundle bundle=new Bundle();

        bundle.putString("APPOINTMENT_ID",appointment_id);
        bundle.putFloat("APPOINTMENT_PRICE",appointment_price);
        bundle.putString("APPOINTMENT_DATE",appointment_date);
        bundle.putString("DOCTOR_ID",doctor_id);


        Intent intent = new Intent (getApplicationContext(), PaymentActivity.class);
        intent.putExtras(bundle);

        startActivity(intent);
    }

}
