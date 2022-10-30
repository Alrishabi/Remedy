package com.hiba.remedy.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
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

public class SecurityQuestionActivity extends AppCompatActivity {
    EditText et_answer,et_password, et_password_confirm,et_id;
    Button b_reset_pass;

    Spinner sp_security_qu;

    ProgressDialog progressDialog;
    RemedyHelper remedyHelper;
    Activity activity;
    URL url;
    LinearLayout mRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_question);
        initiateContext();
        initiateLayout();
    }

    private void initiateContext(){
        activity        = this;
        remedyHelper          = new RemedyHelper(activity);
        progressDialog = new ProgressDialog(activity);
    }

    public  void initiateLayout(){

        et_id       =(EditText)findViewById(R.id.et_id);
        sp_security_qu       =(Spinner)findViewById(R.id.sp_security_qu);
        et_answer               = (EditText) findViewById(R.id.et_answer);
        et_password            = (EditText) findViewById(R.id.et_password);
        et_password_confirm     = (EditText) findViewById(R.id.et_password_confirm);

        b_reset_pass            = (Button) findViewById(R.id.b_reset_pass);

        mRoot                  = (LinearLayout) findViewById(R.id.mRoot);


        setTitle(getString(R.string.recover_your_pass));

        ArrayAdapter<CharSequence> answerAdapter ;
        answerAdapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.security_question_array, android.R.layout.simple_spinner_item);
        answerAdapter.setDropDownViewResource(android.R.layout.simple_expandable_list_item_1);

        sp_security_qu.setAdapter(answerAdapter);

        progressDialog.setMessage(getResources().getString(R.string.recover_pass));


        b_reset_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("ID", et_id.getText().toString());
                Log.d("ANSWER", et_answer.getText().toString());
                Log.d("PASSWORD", et_password.getText().toString());

                resetPassword(Integer.parseInt(et_id.getText().toString()),et_answer.getText().toString(),et_password.getText().toString());
//                startActivity(new Intent(getApplicationContext(),
//                        HomeActivity.class));
            }
        });
    }


    public  void resetPassword(final int id,final String answer,final String password)
    {

        progressDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST,url.BASE_URL+url.RESET_PASSWORD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("response",response);

                            JSONObject jsonObject= new JSONObject(response);
//                                String text [] = response.split(",");
                            if((jsonObject.getString("code").equals("0"))){
                                if(remedyHelper.getValueOfKey("app_language").equals("en")) {
                                    Toast.makeText(activity, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                                }else {
                                    Toast.makeText(activity, jsonObject.getString("message_ar"), Toast.LENGTH_LONG).show();
                                }                                progressDialog.dismiss();

                            }else {
                                if(remedyHelper.getValueOfKey("app_language").equals("en")) {
                                    Toast.makeText(activity, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                                }else {
                                    Toast.makeText(activity, jsonObject.getString("message_ar"), Toast.LENGTH_LONG).show();
                                }//                                Intent i = new Intent(activity,LoginActivity.class);
//                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                startActivity(i);
                                progressDialog.dismiss();
//                                finish();
                            }
                        }catch (Exception e){
                            Log.d("Exception",e.toString());
                            Toast.makeText(getApplicationContext(),e.toString(), Toast.LENGTH_LONG).show();

                            progressDialog.dismiss();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),R.string.no_internet_connection, Toast.LENGTH_LONG).show();
                Snackbar.make(mRoot, R.string.no_internet_connection, Snackbar.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();

                Log.d("ID_", String.valueOf(id));
                Log.d("ANSWER_",answer);
                Log.d("PASSWORD_",password);

                parameters.put("user_id", String.valueOf(id));
                parameters.put("security_qu_answer",answer);
                parameters.put("user_password",password);

//                $user_id = $_POST["user_id"];
//                $security_qu_answer = $_POST["security_qu_answer"];
//                $user_password = $_POST["user_password"];

                return parameters;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);
    }

}
