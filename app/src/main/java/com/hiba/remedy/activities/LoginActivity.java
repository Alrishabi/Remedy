package com.hiba.remedy.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    EditText et_id,et_password;
    TextView tv_show,tv_hide;
    Button b_login;
    RemedyHelper remedyHelper;
    Activity activity;
    URL url;
    LinearLayout mRoot;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initiateContext();

       // set xml items to java variables
        initiateLayout();
    }

    private void initiateContext(){
        activity              = this;
        remedyHelper          = new RemedyHelper(activity);
        progressDialog        = new ProgressDialog(activity);
    }

    public  void initiateLayout(){

        et_id                = (EditText) findViewById(R.id.et_id);
        et_password          = (EditText) findViewById(R.id.et_password);
        tv_hide             = (TextView) findViewById(R.id.tv_hide);
        tv_show              = (TextView) findViewById(R.id.tv_show);
        mRoot                =(LinearLayout) findViewById(R.id.email_login_form);
        b_login             = (Button) findViewById(R.id.b_login);
        progressDialog.setMessage(getResources().getString(R.string.logging_progress_message));

        setTitle(getString(R.string.login_title));
       // if(et_password.setTransformationMethod(new PasswordTransformationMethod())){}
        tv_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_password.setTransformationMethod(null);            }
        });
        tv_hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_password.setTransformationMethod(new PasswordTransformationMethod());
            }
        });

        b_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id=et_id.getText().toString().trim();
                String pass=et_password.getText().toString().trim();

                if(validation(id,pass)){
                   // clearForm();
                    loginMethod(id,pass);
//                    LoginFun();
                }

            }
        });
    }

    public void gotoEmergencySituation(View view){
        startActivity(new Intent(getApplicationContext(),
                EmergencyMapActivity.class));
    }

    public void gotoRegistration(View view){
        startActivity(new Intent(getApplicationContext(),
                RegistrationActivity.class));
    }

    public void gotoSecurityQuestion(View view){
        startActivity(new Intent(getApplicationContext(),
                SecurityQuestionActivity.class));
    }

    private boolean validation(String id , String pass){
        if(id.length() == 0 || pass.length() == 0 ){
            if(id.length() == 0){et_id.setError(getResources().getString(R.string.type_id_num));
                return false;}
            if(pass.length() == 0){et_password.setError(getResources().getString(R.string.type_password));
                return false;}

            return false;
        }
        else{
            if(id.length() != 0){
                if(pass.length() !=0){
                    return true;
                }else{
                    et_password.setError(getResources().getString(R.string.type_password));
                    return false;
                }
            }else{
                et_id.setError(getResources().getString(R.string.type_id_num));
                return false;
            }
        }
    }

    public void loginMethod(final String id, final String pass){
            progressDialog.show();
            StringRequest request = new StringRequest(Request.Method.POST,url.BASE_URL+url.LOGIN_ADMIN_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                               JSONObject jsonObject= new JSONObject(response);
//                                String text [] = response.split(",");
                                if((jsonObject.getString("code").equals("Login_failed"))){
                                    Toast.makeText(getApplicationContext(),R.string.Login_failed, Toast.LENGTH_LONG).show();
                                    progressDialog.dismiss();
                                }else {
                                    remedyHelper.log("response",response.toString().trim());

                                    remedyHelper.setValueToKey("is_login","1");
                                    remedyHelper.setValueToKey("user_id",jsonObject.getString("user_id"));
                                    remedyHelper.setValueToKey("user_name",jsonObject.getString("user_name"));
                                    remedyHelper.setValueToKey("user_full_name",jsonObject.getString("user_full_name"));
                                    remedyHelper.setValueToKey("user_phone",jsonObject.getString("user_phone"));
                                    remedyHelper.setValueToKey("security_qu_answer",jsonObject.getString("security_qu_answer"));
                                    remedyHelper.log("USER_CATEGORY",jsonObject.getString("user_category"));
                                    remedyHelper.setValueToKey("user_category",jsonObject.getString("user_category"));
                                    remedyHelper.setValueToKey("user_state",jsonObject.getString("user_state"));

                                    progressDialog.dismiss();

                                    remedyHelper.log("USER",remedyHelper.getValueOfKey("user_id"));

                                    if(remedyHelper.getValueOfKey("user_category").equals("1"))
                                    {
                                        remedyHelper.setValueToKey("Hospital_ID_KEY","No_hospital");
                                        Intent i = new Intent(activity,HomeActivity.class);
                                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(i);
                                    }
                                    else if(remedyHelper.getValueOfKey("user_category").equals("2"))
                                    {
                                        Intent i = new Intent(activity, HospitalsAccountActivity.class);
                                        //i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(i);

                                    }else if(remedyHelper.getValueOfKey("user_category").equals("3"))
                                    {
                                        Intent i = new Intent(activity,DoctorsActivity.class);
                                       // i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(i);
                                    }else if(remedyHelper.getValueOfKey("user_category").equals("4"))
                                    {
                                        Intent i = new Intent(activity,LaboratoryUserActivity.class);
                                        // i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(i);
                                    }else if(remedyHelper.getValueOfKey("user_category").equals("5"))
                                    {
                                        Intent i = new Intent(activity,PharmacistUserActivity.class);
                                        // i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(i);
                                    }

                                   // clearForm();
                                    progressDialog.dismiss();
                                    finish();
                                }
                            }catch (Exception e){
                                Log.d("Exception",e.toString());

                                progressDialog.dismiss();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(),R.string.no_internet_connection, Toast.LENGTH_LONG).show();
                    Snackbar.make(mRoot,R.string.no_internet_connection, Snackbar.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> parameters = new HashMap<String, String>();

                    parameters.put("user_id",id);
                    parameters.put("user_password",pass);

                    return parameters;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(request);
    }

    public void clearForm(){
        et_id.setText("");
        et_password.setText("");
    }

}
