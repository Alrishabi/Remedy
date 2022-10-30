package com.hiba.remedy.activities;

import android.app.Activity;
import android.app.ProgressDialog;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {

    EditText et_full_name,et_phone_number,et_pass,et_pass_confirm,et_answer_qu,et_id;
    Spinner sp_security_qu,sp_user_category,sp_state;
    Button b_signup;
    LinearLayout mRoot;

    RemedyHelper remedyHelper;
    Activity activity;
    ProgressDialog progressDialog;

    List<String> stateList;
    List<String> categoryList;


    URL url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        initiateContext();
        initiateLayout();

    }
    private void initiateContext(){
        activity        = this;
        remedyHelper          = new RemedyHelper(activity);
        progressDialog = new ProgressDialog(activity);
        stateList = new ArrayList<String>();
        categoryList = new ArrayList<String>();
    }

    public  void initiateLayout(){

        stateList.add(getString(R.string.select_state));
        stateList.add(getString(R.string.khartum));
        stateList.add(getString(R.string.bahre));
        stateList.add(getString(R.string.omdurman));


        categoryList.add(getString(R.string.select_category));
        categoryList.add(getString(R.string.patient_user));
        categoryList.add(getString(R.string.hospital_user));
        categoryList.add(getString(R.string.doctor_user));
        categoryList.add(getString(R.string.labtorty_user));
        categoryList.add(getString(R.string.pharmasict_user));



        et_full_name              = (EditText) findViewById(R.id.et_full_name);
        et_id                     = (EditText) findViewById(R.id.et_ID);
        et_phone_number           = (EditText) findViewById(R.id.et_phone_number);
        et_pass                   = (EditText) findViewById(R.id.et_pass);
        et_pass_confirm           = (EditText) findViewById(R.id.et_pass_confirm);
        sp_security_qu            = (Spinner) findViewById(R.id.sp_security_qu);
        sp_user_category            = (Spinner) findViewById(R.id.sp_user_category);
        sp_state            = (Spinner) findViewById(R.id.sp_state);
        et_answer_qu              = (EditText) findViewById(R.id.et_answer_qu);
        b_signup                  = (Button) findViewById(R.id.b_signup);
        mRoot                  = (LinearLayout) findViewById(R.id.rootView);
        setTitle(getString(R.string.registration));

        ArrayAdapter<CharSequence> answerAdapter ;
        answerAdapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.security_question_array, android.R.layout.simple_spinner_item);
        answerAdapter.setDropDownViewResource(android.R.layout.simple_expandable_list_item_1);

        sp_security_qu.setAdapter(answerAdapter);

        ArrayAdapter<String> stateAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, stateList);
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,categoryList);

        // Drop down layout style - list view with radio button
        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        sp_state.setAdapter(stateAdapter);
        sp_user_category.setAdapter(categoryAdapter);


        progressDialog.setMessage(getResources().getString(R.string.registration));


        b_signup.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {

                String fullname =et_full_name.getText().toString().trim();
                String state =sp_state.getSelectedItem().toString().trim();
                String id =et_id.getText().toString().trim();
                String phone_num =et_phone_number.getText().toString().trim();
                long user_category =sp_user_category.getSelectedItemId();
                String pass =et_pass.getText().toString().trim();
                String confirmPass =et_pass.getText().toString().trim();
                String answer =et_answer_qu.getText().toString().trim();


                           Log.d("ID", id);
                            Log.d("FULLNAME",fullname);
                            Log.d("PHONENUMBER",phone_num);
                            Log.d("ANSWER",answer);
                            Log.d("BEFORCALLMETHOD","B");

                if(validation(fullname,id,phone_num,pass,confirmPass,answer)){

                    registrationMethod(fullname,state,id,phone_num,user_category,pass,answer);
//                    startActivity(new Intent(getApplicationContext(),
//                            HomeActivity.class));
                }

            }
        });
    }
    public void gotoLogin(View view){
        clearForm();
        startActivity(new Intent(getApplicationContext(),
                LoginActivity.class));
        finish();
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private boolean validation(String fullName, String id, String phone_num, String pass, String confirmPass, String answer) {
        Boolean validate = false;
        // check if transaction from account or card , ACCC mean account transaction

        // transaction is from card
        if (fullName.length() == 0 ||sp_state.getSelectedItemId()==0|| id.length() == 0 || phone_num.length() == 0 ||phone_num.length() < 10 ||
                sp_user_category.getSelectedItemId() == 0 ||  sp_security_qu.getSelectedItemId() == 0 || pass.length() == 0 || confirmPass.length() == 0 || answer.length() == 0) {
            if (fullName.length() == 0) {
                et_full_name.setError(getResources().getString(R.string.full_name), getDrawable(R.drawable.ic_qution_tabe_icon));
                Toast.makeText(getApplicationContext(), R.string.full_name, Toast.LENGTH_LONG).show();
                return false;
            }
            if (sp_state.getSelectedItemId() == 0){
//                    sp_security_qu.set("",getDrawable(R.drawable.ic_qution_tabe_icon));
                Toast.makeText(getApplicationContext(), R.string.please_select_state, Toast.LENGTH_LONG).show();
                return false;
            }
            if (id.length() == 0) {
                et_id.setError(getResources().getString(R.string.type_id_num), getDrawable(R.drawable.ic_qution_tabe_icon));
                Toast.makeText(getApplicationContext(), R.string.type_id_num, Toast.LENGTH_LONG).show();
                return false;
            }
            if (phone_num.length() == 0) {
                et_phone_number.setError(getResources().getString(R.string.title_phone), getDrawable(R.drawable.ic_qution_tabe_icon));
                Toast.makeText(getApplicationContext(), R.string.title_phone, Toast.LENGTH_LONG).show();
                return false;
            }
            if (phone_num.length() <10) {
                et_phone_number.setError(getResources().getString(R.string.phone_less_than_ten), getDrawable(R.drawable.ic_qution_tabe_icon));
                Toast.makeText(getApplicationContext(), R.string.title_phone, Toast.LENGTH_LONG).show();
                return false;
            }
            if (sp_user_category.getSelectedItemId() == 0) {
//                    sp_security_qu.set("",getDrawable(R.drawable.ic_qution_tabe_icon));
                Toast.makeText(getApplicationContext(), R.string.please_select_user_category, Toast.LENGTH_LONG).show();
                return false;
            }
            if (pass.length() == 0) {
                et_pass.setError(getResources().getString(R.string.type_password), getDrawable(R.drawable.ic_qution_tabe_icon));
                Toast.makeText(getApplicationContext(), R.string.type_password, Toast.LENGTH_LONG).show();
                return false;
            }
            if (confirmPass.length() == 0) {
                et_pass_confirm.setError(getResources().getString(R.string.confirm_password_hint), getDrawable(R.drawable.ic_qution_tabe_icon));
                Toast.makeText(getApplicationContext(), R.string.confirm_password_hint, Toast.LENGTH_LONG).show();
                return false;
            }
            if (confirmPass.equals(pass)) {
                et_pass.setError(getResources().getString(R.string.type_password), getDrawable(R.drawable.ic_qution_tabe_icon));
                et_pass_confirm.setError(getResources().getString(R.string.confirm_password_hint), getDrawable(R.drawable.ic_qution_tabe_icon));
                Toast.makeText(getApplicationContext(), R.string.pass_dosnot_much, Toast.LENGTH_LONG).show();
                return false;
            }
            if (sp_security_qu.getSelectedItemId() == 0) {
//                    sp_security_qu.set("",getDrawable(R.drawable.ic_qution_tabe_icon));
                Toast.makeText(getApplicationContext(), R.string.select_qustion, Toast.LENGTH_LONG).show();
                return false;
            }
            if (answer.length() == 0) {
                et_answer_qu.setError(getResources().getString(R.string.answer_qution), getDrawable(R.drawable.ic_qution_tabe_icon));
                Toast.makeText(getApplicationContext(), R.string.answer_qution, Toast.LENGTH_LONG).show();
                return false;
            }


            return false;
        } else {
            if (fullName.length() != 0) {
               if (sp_state.getSelectedItemId() != 0) {
                 if (id.length() != 0) {
                    if (phone_num.length() ==10) {
                      if (sp_user_category.getSelectedItemId() != 0) {
                        if (pass.length() != 0) {
                            if (confirmPass.length() != 0) {
                                if (pass.equals(confirmPass)) {
                                    if (sp_security_qu.getSelectedItemId() != 0) {
                                        if (answer.length() != 0) {
                                            validate = true;
                                            return validate;
                                        } else {
                                            Toast.makeText(getApplicationContext(), R.string.answer_qution, Toast.LENGTH_LONG).show();
                                            et_answer_qu.setError(getResources().getString(R.string.answer_qution));
                                            validate = false;
                                            return validate;
                                        }
                                    } else {
                                        Toast.makeText(getApplicationContext(), R.string.select_qustion, Toast.LENGTH_LONG).show();
                                        validate = false;
                                        return validate;
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), R.string.pass_dosnot_much, Toast.LENGTH_LONG).show();
                                    et_pass.setError(getResources().getString(R.string.answer_qution), getDrawable(R.drawable.ic_qution_tabe_icon));
                                    et_pass_confirm.setError(getResources().getString(R.string.answer_qution));
                                    validate = false;
                                    return validate;
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), R.string.confirm_password_hint, Toast.LENGTH_LONG).show();
                                et_pass_confirm.setError(getResources().getString(R.string.confirm_password_hint));

                                validate = false;
                                return validate;
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), R.string.enter_pass, Toast.LENGTH_LONG).show();
                            et_pass.setError(getResources().getString(R.string.enter_pass));
                            validate = false;
                            return validate;
                        }

                      }else {
                         Toast.makeText(getApplicationContext(), R.string.please_select_user_category, Toast.LENGTH_LONG).show();
                        validate = false;
                        return validate;
                    }
                    } else {
                        Toast.makeText(getApplicationContext(), R.string.phone_less_than_ten, Toast.LENGTH_LONG).show();
                        et_phone_number.setError(getResources().getString(R.string.phone_less_than_ten));
                        validate = false;
                        return validate;
                    }
                } else {
                    Toast.makeText(getApplicationContext(), R.string.type_id_num, Toast.LENGTH_LONG).show();
                    et_id.setError(getResources().getString(R.string.type_id_num));
                    validate = false;
                    return validate;
                }
              } else {
                Toast.makeText(getApplicationContext(), R.string.please_select_state, Toast.LENGTH_LONG).show();
                et_id.setError(getResources().getString(R.string.type_id_num));
                validate = false;
                return validate;
                  }
            }
            else {
                Toast.makeText(getApplicationContext(), R.string.full_name, Toast.LENGTH_LONG).show();
                et_full_name.setError(getResources().getString(R.string.full_name));
                validate = false;
                return validate;
            }
        }
    }
                public void clearForm () {
                    et_id.setText("");
                    sp_state.setSelection(0);
                    et_full_name.setText("");
                    et_answer_qu.setText("");
                    et_pass_confirm.setText("");
                    et_pass.setText("");
                    et_phone_number.setText("");
                    sp_user_category.setSelection(0);
                    sp_security_qu.setSelection(0);

                }

                public void registrationMethod ( final String fullName,final String state, final String id,
                final String phone_num,final long category, final String pass, final String answer){

                    progressDialog.show();
                    StringRequest request = new StringRequest(Request.Method.POST, URL.BASE_URL + url.REGISTRATION_URL,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {

                                        Log.d("ID", id);
                                        Log.d("FULLNAME", fullName);
                                        Log.d("PHONENUMBER", phone_num);
                                        Log.d("ANSWER", answer);
                                        Log.d("response", response);

                                        JSONObject jsonObject = new JSONObject(response);

                                        Log.d("jsonObject", jsonObject.toString());
//                                String text [] = response.split(",");
                                        if ((jsonObject.getString("code").equals("duplicate"))) {
                                            if(remedyHelper.getValueOfKey("app_language").equals("en")) {
                                                Toast.makeText(activity, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                                            }else {
                                                Toast.makeText(activity, jsonObject.getString("message_ar"), Toast.LENGTH_LONG).show();
                                            }
                                            progressDialog.dismiss();
                                        } else if (jsonObject.getString("code").equals("registration_failed")) {
                                            if(remedyHelper.getValueOfKey("app_language").equals("en")) {
                                                Toast.makeText(activity, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                                            }else {
                                                Toast.makeText(activity, jsonObject.getString("message_ar"), Toast.LENGTH_LONG).show();
                                            }
                                            progressDialog.dismiss();
                                        } else {

                                            Intent i = new Intent(activity, LoginActivity.class);

                                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                            if(remedyHelper.getValueOfKey("app_language").equals("en")) {
                                                Toast.makeText(activity, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                                            }else {
                                                Toast.makeText(activity, jsonObject.getString("message_ar"), Toast.LENGTH_LONG).show();
                                            }
                                            remedyHelper.setValueToKey("is_login", "1");
                                            remedyHelper.setValueToKey("user_id", String.valueOf(id));
                                            remedyHelper.setValueToKey("user_name", fullName);
                                            remedyHelper.setValueToKey("user_phone", phone_num);
                                            remedyHelper.setValueToKey("security_qu_answer", answer);

                                            Log.d("USER", remedyHelper.getValueOfKey("user_id"));

                                            startActivity(i);
                                            //clearForm();
                                            progressDialog.dismiss();
                                            finish();
                                        }
                                    } catch (JSONException e) {
                                        Log.d("JSONException", e.toString());
                                        Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                                        progressDialog.dismiss();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), R.string.no_internet_connection, Toast.LENGTH_LONG).show();
                            Snackbar.make(mRoot, R.string.no_internet_connection, Snackbar.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> parameters = new HashMap<String, String>();

                            Log.d("PARAMETERS", "ENTERED");
                            Log.d("ID", id);
                            Log.d("FULLNAME", fullName);
                            Log.d("PHONENUMBER", phone_num);
                            Log.d("PASSWORD", pass);
                            Log.d("ANSWER", answer);

                            parameters.put("user_id",String.valueOf(id));
                            parameters.put("user_name", fullName);
                            parameters.put("user_state", state);
                            parameters.put("user_password", pass);
                            parameters.put("user_phone", phone_num);
                            parameters.put("user_category", String.valueOf(category));
                            parameters.put("security_qu_answer", answer);

                            return parameters;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    requestQueue.add(request);

                }
            }


