package com.hiba.remedy.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class PaymentActivity extends AppCompatActivity {

    Activity activity;
    RemedyHelper remedyHelper;
    ProgressDialog progressDialog;
    private  int Rnumber;
    EditText et_pan,et_amount,et_exp_date,et_ipin;
    Button bt_pay;
    private Random Number;
    URL url;
    public float    appointment_price;
    public String    appointment_id,appointment_date,doctor_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        initiateContext();
        initiateLayout();
    }

    private void initiateContext(){
        activity           = this;
        remedyHelper         = new RemedyHelper(activity);
        progressDialog = new ProgressDialog(activity);
    }

    public  void initiateLayout(){

        et_pan                 = (EditText) findViewById(R.id.et_pan);
        et_amount              = (EditText) findViewById(R.id.et_amount);
        et_exp_date            = (EditText) findViewById(R.id.et_date);
        et_ipin                 = (EditText) findViewById(R.id.et_ipin);
        bt_pay                  = (Button) findViewById(R.id.btn_pay);

        setTitle(getString(R.string.electronic_payment));

        progressDialog.setMessage(getString(R.string.get_appointment)+"...");


        //   Receive DATA
try{

    appointment_price        = getIntent().getExtras().getFloat("APPOINTMENT_PRICE");
    appointment_id        = getIntent().getExtras().getString("APPOINTMENT_ID");
    appointment_date        = getIntent().getExtras().getString("APPOINTMENT_DATE");
    doctor_id        = getIntent().getExtras().getString("DOCTOR_ID");



    addText(et_amount,appointment_price+" SDG");
    //et_amount.setText(appointment_price +" SDG");
     et_amount.setEnabled(false);

    Log.d("appointment_id_in_pay", appointment_id);
    Log.d("appointment_price_in_p", String.valueOf(appointment_price));

      }catch (Exception e){
    e.printStackTrace();
    Log.d("Exception", e.toString());
     }


        et_exp_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//        createDialogWithoutDateField().show();
                MonthYearPickerDialog pickerDialog = new MonthYearPickerDialog();
                pickerDialog.setListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int i2) {
                        String y=String.valueOf(year);
                        if(month>9) {
                           // Toast.makeText(activity, y.substring(2) + month, Toast.LENGTH_SHORT).show();
                            et_exp_date.setText(y.substring(2) + month);
                        }else {
//                            Toast.makeText(activity, y.substring(2) + "0"+month, Toast.LENGTH_SHORT).show();
                            et_exp_date.setText(y.substring(2) + "0"+month);
                        }
                    }
                });
                pickerDialog.show(getSupportFragmentManager(), "Expire date picker ");
            }
        });

        bt_pay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Number = new Random();
                Rnumber = Number.nextInt(10000);

               // Toast.makeText(getApplicationContext(),String.valueOf(Rnumber), Toast.LENGTH_SHORT).show();
                Log.d("ONCLICK","entered");


//                if(validation(et_pan.getText().toString().trim(),et_exp_date.getText().toString().trim(),et_ipin.getText().toString().trim())){

                payMethod(String.valueOf(Rnumber),et_pan.getText().toString().trim(),
                        appointment_id,et_exp_date.getText().toString(),et_ipin.getText().toString(), String.valueOf(appointment_price),remedyHelper.getValueOfKey("user_id"));
//                }

//

            }
        });
    }
    public Boolean validation(String pan, String expiry_date, String ipin){

            if(pan.length() == 0 || expiry_date.length() == 0 || ipin.length() == 0){
                if(pan.length() == 0){
                    et_pan.setError(getResources().getString(R.string.enter_pan));
                    Toast.makeText(activity,R.string.enter_pan, Toast.LENGTH_LONG).show();
                return false;}
                if(expiry_date.length() == 0){et_exp_date.setError(
                        getResources().getString(R.string.type_date));
                    Toast.makeText(activity,R.string.type_date, Toast.LENGTH_LONG).show();
                return false;
                }
                et_ipin.setError(getResources().getString(R.string.enter_your_ipin));
                Toast.makeText(activity,R.string.enter_your_ipin, Toast.LENGTH_LONG).show();
                return false;

            }else{
                if(pan.length() == 16 || pan.length() == 19){
                    if(expiry_date.length() == 4){
                        if(ipin.length() == 4){
                            return true;
                        }else {
                            et_ipin.setError(getResources().getString(R.string.enter_ipin));
                            Toast.makeText(activity,R.string.enter_ipin, Toast.LENGTH_LONG).show();
                            return false;
                        }
                    }else{
                        getResources().getString(R.string.type_date);
                        Toast.makeText(activity,R.string.type_date, Toast.LENGTH_LONG).show();
                        return false;
                    }
                }else{
                    et_pan.setError(getResources().getString(R.string.enter_pan_correctly));
                    Toast.makeText(activity,R.string.enter_pan_correctly, Toast.LENGTH_LONG).show();
                    return false;
                }
            }

    }

    public void addText(EditText textView,String text){
        textView.append(" "+text);
        //textView.setVisibility(View.VISIBLE);
    }

        public void payMethod(final String txr, final String pan,final String to, final String exp_date,
                              final String ipin, final String amount,final String user_id)
//    $from_card =$_POST["from_card"];
//    $expire_date =$_POST["expire_date"];
//    $ipin =$_POST["ipin"];
//    $amount =$_POST["amount"];
//
//    $trx_id =$_POST["trx_id"];
//    $to =$_POST["to"];
//    $status =$_POST["status"];
//
//    $user_id =$_POST["user_id"]
    {
            progressDialog.show();

            Log.d("txr",txr);
            Log.d("pan",pan);
            Log.d("exp_date",exp_date);
            Log.d("ipin",ipin);
            Log.d("amount",amount);
            Log.d(" user_id",user_id);

            StringRequest request = new StringRequest(Request.Method.POST,url.BASE_URL+url.PAY_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("RESPONSE",response);

                            try {
                                JSONObject jsonObject= new JSONObject(response);
                                Log.d("jsonObject",jsonObject.toString());
                                if(jsonObject.getString("code").equals("0")){
                                    if(remedyHelper.getValueOfKey("app_language").equals("en")) {
                                        Toast.makeText(activity, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                                    }else {
                                        Toast.makeText(activity, jsonObject.getString("message_ar"), Toast.LENGTH_LONG).show();
                                    }
                                    finish();
                                    //   progressDialog.dismiss();
                                }else {
                                    if(remedyHelper.getValueOfKey("app_language").equals("en")) {
                                        Toast.makeText(activity, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                                    }else {
                                        Toast.makeText(activity, jsonObject.getString("message_ar"), Toast.LENGTH_LONG).show();
                                    }
                                    Toast.makeText(getApplicationContext(),jsonObject.getString("Error"), Toast.LENGTH_LONG).show();
                                    //  progressDialog.dismiss();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.d("JSONException",e.toString());
                                Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
                                progressDialog.dismiss();
                            }
                            progressDialog.dismiss();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(),R.string.payment_failed, Toast.LENGTH_LONG).show();
//                   Snackbar.make(mRoot, R.string.no_internet_connection,Snackbar.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> parameters = new HashMap<String, String>();

                    Log.d("trx_id",pan);
                    Log.d("from_card",exp_date);
                    Log.d("to",ipin);
                    Log.d("expire_date",amount);
                    Log.d("amount",amount);
                    Log.d("ipin",amount);
                    Log.d("user_id",amount);


                    parameters.put("trx_id",txr);
                    parameters.put("from_card",pan);
                    parameters.put("to",to);
                    parameters.put("expire_date",exp_date);
                    parameters.put("amount",amount);
                    parameters.put("ipin",ipin);
                    parameters.put("user_id",user_id);
                    parameters.put("appointment_date",appointment_date);
                    parameters.put("doctor_id",doctor_id);


//                    $trx_id =$_POST["trx_id"];
//                    $from_card =$_POST["from_card"];
//                    $to =$_POST["to"];
//                    $expire_date =$_POST["expire_date"];
//                    $amount =$_POST["amount"];
//                    $ipin =$_POST["ipin"];
//                    $user_id =$_POST["user_id"];

//                    `trx_id`, `from_card`, `to`, `status`, `amount`
//                    INSERT INTO `bank_table` (`account_id`, `account_number`, `card_number`, `expire_date`, `ipin`, `balance`, `user_id`)


                    return parameters;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(request);
        }




}
