package com.hiba.remedy.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.LoginFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
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
import com.hiba.remedy.activities.GetAnAppointmentActivity;
import com.hiba.remedy.activities.PatientAppointmentActivity;
import com.hiba.remedy.adapters.helper.AppointmentList;
import com.hiba.remedy.adapters.helper.DoctorsList;
import com.hiba.remedy.helpers.RemedyHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;


/**
 * Created by musab on 8/21/2017.
 */

public class Edit_Dialog_Fragment extends AppCompatDialogFragment {
    Activity activity;
    PatientAppointmentActivity patientAppointmentActivity;
    URL url;
    EditText et_patient_name, et_phone_number, et_date, doc_name ,doc_phone , ed_price , et_appo_address ;
     Button saveB, cancelB;
    final Calendar cel= Calendar.getInstance();
    final Calendar myCalendar = Calendar.getInstance();
    RemedyHelper remedyHelper;


    DatePickerDialog.OnDateSetListener ddate =new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            //TO
            cel.set( year,monthOfYear,dayOfMonth);
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
            String myFormat=sdf.format(cel.getTime());
            et_date.setText(myFormat);        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.edit_dialog, container, false);
        remedyHelper=new RemedyHelper(getActivity());
        patientAppointmentActivity=new PatientAppointmentActivity();

        initLayout(rootView);

        return rootView;
    }
//    public Edit_Dialog_Fragment(Activity activity)
//    {
//        this.activity=activity;
//        remedyHelper=new RemedyHelper(activity);
//    }
    public void initLayout(View rootView){

        et_patient_name =  rootView.findViewById(R.id.et_patient_name);
        et_phone_number =  rootView.findViewById(R.id.et_phone_number);
        et_date =  rootView.findViewById(R.id.et_date);
//        Doctor_info
        doc_name =  rootView.findViewById(R.id.doc_name);
        doc_phone =  rootView.findViewById(R.id.doc_phone);
        ed_price =  rootView.findViewById(R.id.ed_price);
        et_appo_address =  rootView.findViewById(R.id.et_appo_address);

        saveB = (Button) rootView.findViewById(R.id.save);
        cancelB = (Button) rootView.findViewById(R.id.btn_cancel);


//        final String id = this.getArguments().getString("ID_KEY");

        assert this.getArguments() != null;
        final int      appointment_id        = this.getArguments().getInt("APPOINTMENT_ID_KEY");
        final String   patient_name          = this.getArguments().getString("PATIENT_KEY");
        final String   phone_number          = this.getArguments().getString("PATIENT_PHONE_KEY");
        final String   date                  = this.getArguments().getString("APPOIN_DATE_KEY");
        final String   docname              = this.getArguments().getString("DOC_NAME_KEY");
        final String   docphone             = this.getArguments().getString("DOC_PHONE_KEY");
        final String   price                 = this.getArguments().getString("PRICE_KEY");
        final String   appo_address          = this.getArguments().getString("APPOINT_ADDRESS_KEY");
        Log.d("appointment_id", String.valueOf(appointment_id));
//        itemList = appointmentLists.get(1);
//
        et_patient_name.setText(patient_name);
        et_phone_number.setText(phone_number);
        et_date.setText(date);
//        Doctor_info
        doc_name.setText(docname);
        doc_phone.setText(docphone);
        ed_price.setText(price);
        et_appo_address.setText(appo_address);

        saveB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             et_patient_name.getText();
             et_phone_number.getText();
        et_date.getText();
               upDateMethod(et_patient_name.getText().toString().trim(),et_phone_number.getText().toString().trim(), String.valueOf(appointment_id),et_date.getText().toString().trim());
            }
        });
        cancelB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        final DatePickerDialog.OnDateSetListener date_cal = new DatePickerDialog.OnDateSetListener() {
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
                new DatePickerDialog(getContext(), date_cal, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void upDateMethod(final String patient_name,final String user_phone,final String appointment_id,final String appointment_date ) {
//        if(lable.length() > 0  && !typeSpinner.getSelectedItem().toString().equalsIgnoreCase(getResources().getString(R.string.ignore_case_for_broadcast))
//                && content.length() > 0 && dateP.length()>0) {
            final ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage(getResources().getString(R.string.update_progress_message));
            progressDialog.show();

            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    URL.BASE_URL+URL.UPDATE_APPOINTMENT_URL,
                    new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("RESPONSE_IN_spcific_doc",response);
                    try {
                        JSONObject jsonObject= new JSONObject(response);

                        if(jsonObject.getString("code").equals("0")){
                            if(remedyHelper.getValueOfKey("app_language").equals("en")) {
                                Toast.makeText(getContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                            }else {
                                Toast.makeText(getContext(), jsonObject.getString("message_ar"), Toast.LENGTH_LONG).show();
                            }
//                            notifyItemRemoved(position);
//                            notifyItemRangeChanged(position,appointmentLists.size());
//                            notifyDataSetChanged();

                            patientAppointmentActivity.setRecyclerviewAppointment();
                            getDialog().dismiss();

                        }else {
                            if(remedyHelper.getValueOfKey("app_language").equals("en")) {
                                Toast.makeText(getContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                                Toast.makeText(getContext(), jsonObject.getString("error"), Toast.LENGTH_LONG).show();
                            }else {
                                Toast.makeText(getContext(), jsonObject.getString("message_ar"), Toast.LENGTH_LONG).show();
                                Toast.makeText(getContext(), jsonObject.getString("error"), Toast.LENGTH_LONG).show();
                            }
                        }
                        progressDialog.dismiss();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getContext(), getResources().getString(R.string.sorry)+"\n"
                            +getResources().getString(R.string.connection_failed), Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> parameters = new HashMap<>();

                    parameters.put("appointment_id",appointment_id);
                    parameters.put("patient_name",patient_name);
                    parameters.put("user_phone",user_phone);
                    parameters.put("appointment_date",appointment_date);



//                    params.put("broadcast_id", String.valueOf(i).trim());
//                    params.put("broadcast_type",typeSpinner.getSelectedItem().toString().trim());
//                    params.put("broadcast_title", lable.getText().toString().trim());
//                    params.put("broadcast_content",content.getText().toString().trim());
//                    params.put("broadcast_date",dateP.getText().toString().trim());

                    return parameters;
                }
            };
            requestQueue.add(stringRequest);
//        }
//        else{
//            if(lable.length() == 0){
//                lable.setError(getResources().getString(R.string.type_broadcast_lable));
//            }else if(typeSpinner.getSelectedItem().toString().equalsIgnoreCase(getResources().getString(R.string.ignore_case_for_broadcast))){
//                Toast.makeText(getContext(),R.string.choose_broadcast_type, Toast.LENGTH_LONG).show();
//            }else if(content.length() == 0){
//                content.setError(getResources().getString(R.string.type_broadcast_contanet));
//            }
//        }
    }

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        et_date.setText(sdf.format(myCalendar.getTime()));
    }
}
