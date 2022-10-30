package com.hiba.remedy.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
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
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {
    LinearLayout mRoot;
    RemedyHelper remedyHelper;
    Activity activity;
    ProgressDialog progressDialog;

    TextView tv_hospital_id,tv_hospital_name,tv_hospital_state,tv_hospital_specialization,tv_hospital_phone,tv_hospital_address,tv_hospital_location,tv_price;
    ImageView iv_hospital_image;
    RatingBar tv_hospital_rate;
    LinearLayout ll_location,ll_rate,ll_price;
    Button    b_call,b_appointment,b_icu,b_doctors;
    int MY_PERMISSIONS_REQUEST_CALL_PHONE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initiateContext();
        initiateLayout();
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    private void initiateContext(){
        activity        = this;
        remedyHelper    = new RemedyHelper(activity);
        progressDialog  = new ProgressDialog(activity);
    }

    public  void initiateLayout(){

        tv_hospital_id            = findViewById(R.id.id);
        tv_hospital_name          = findViewById(R.id.name);
        tv_hospital_specialization  = findViewById(R.id.specialization);
        tv_hospital_state         = findViewById(R.id.state);
        tv_price                  = findViewById(R.id.tv_price);
        iv_hospital_image         = findViewById(R.id.image);
        tv_hospital_phone         = findViewById(R.id.phone);
        tv_hospital_address       = findViewById(R.id.address);
        tv_hospital_location      = findViewById(R.id.location);
        ll_location               = findViewById(R.id.location_liner);
        ll_rate                   = findViewById(R.id.liner_rate);
        ll_price                  = findViewById(R.id.ll_price);
        tv_hospital_rate          = findViewById(R.id.ratebare);

        b_call                    = findViewById(R.id.b_call);
        b_appointment             = findViewById(R.id.btn_appointment);
        b_icu                     = findViewById(R.id.btn_icu);
        b_doctors                 = findViewById(R.id.b_doctors);
        mRoot                     = findViewById(R.id.mRoot);

        setTitle(getString(R.string.profile));

//        ArrayAdapter<CharSequence> answerAdapter ;
//        answerAdapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.security_question_array, android.R.layout.simple_spinner_item);
//        answerAdapter.setDropDownViewResource(android.R.layout.simple_expandable_list_item_1);

        //sp_security_qu.setAdapter(answerAdapter);

//        progressDialog.setMessage(getResources().getString(R.string.registration));

        //Receive DATA
        final int   category             = getIntent().getExtras().getInt("ID_CATEGORY");
        final int    id                  = getIntent().getExtras().getInt("ID_KEY");
        final String name                = getIntent().getExtras().getString("NAME_KEY");
        final String image_url           = getIntent().getExtras().getString("IMAGE_URL_KEY");
        final String bContent            = getIntent().getExtras().getString("SPECIALIZATION_KEY");
        final String the_state           = getIntent().getExtras().getString("STATE_KEY");
        final float price                = getIntent().getExtras().getFloat("PRICE_KEY");
        final String phone               = getIntent().getExtras().getString("PHONE_KEY");
        final String address             = getIntent().getExtras().getString("ADDRESS_KEY");
        final String locationKey         = getIntent().getExtras().getString("LOCATION_KEY");
        final float  rate                = getIntent().getExtras().getFloat("RATE_KEY");

        if(category==2){
            b_appointment.setVisibility(View.VISIBLE);
            ll_price.setVisibility(View.VISIBLE);
            ll_rate.setVisibility(View.GONE);
            ll_location.setVisibility(View.GONE);
        }else if(category==1 ){
            ll_price.setVisibility(View.GONE);
            b_icu.setVisibility(View.VISIBLE);
            b_doctors.setVisibility(View.VISIBLE);
        }else if(category==3 || category==4  ){
            ll_price.setVisibility(View.GONE);
        }


        if(image_url!=null && image_url.length()>0 ){
            Picasso.get().load(image_url).placeholder(R.drawable.helloandroid).into(iv_hospital_image);
        }else{
            Toast.makeText(getBaseContext(),"No image to view",Toast.LENGTH_LONG).show();

            Picasso.get().load(R.drawable.helloandroid).into(iv_hospital_image);
        }

        Log.d("ID", String.valueOf(id));
//      Log.d("Hospital_ID_KEY", hospital_id);
        Log.d("NAME",name);
        Log.d("IMAGE_URL_KEY", URL.BASE_URL +image_url);
        Log.d("SPECIALIZATION", bContent);
        Log.d("PHONE_KEY",phone);
        Log.d("PRICE_KEY", String.valueOf(price));
        Log.d("ADDRESS_KEY",address);
        Log.d("LOCATION_KEY", locationKey);
        Log.d("RATE_KEY", String.valueOf(rate));


        tv_hospital_id.setText(String.valueOf(id));
        tv_hospital_name.setText(name);
        tv_hospital_specialization.setText(bContent);
        tv_hospital_state.setText(the_state);
        tv_hospital_phone.setText(phone);
        tv_price.setText(String.valueOf(price));
        tv_hospital_address.setText(address);
        tv_hospital_location.setText(locationKey);
        tv_hospital_rate.setRating(rate);


        b_appointment.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                goToAppointmentActivity(id,name,phone,address,price);
            }
        });
        b_doctors.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                goToDisplayDocFromHospitalActivity(name,phone,address,price);
            }
        });
        b_icu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //goToAppointmentActivity(id,name,phone,address,price);
                new AlertDialog.Builder(activity)
                        .setIcon(R.drawable.ic_attention)
                        .setTitle(R.string.alert_icu_room)
                        .setMessage(activity.getResources().getString(R.string.icu_qution))
                        .setPositiveButton(activity.getResources().getString(R.string.yes),new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                getAnIcuRoom(String.valueOf(id),remedyHelper.getValueOfKey("user_phone"),remedyHelper.getValueOfKey("user_id"));

//                                try {
////                                    Log.d("APPOINTMENT_ID_insert", jsonObject.getString("appointment_id"));
//                                  //  goToPaymentActivity(jsonObject.getString("appointment_id"), appointment_price1);
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
                               // finish();
                            }

                        }).setNegativeButton(R.string.no,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                        .show();
            }
        });
        b_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                call(phone);
            }
        });

        tv_hospital_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                call(phone);
            }
        });
    }
    public void call(String main_contact) {
//        String main_contact = "2955";
        Log.d("Call",main_contact);
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:"+main_contact));
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.CALL_PHONE},
                    MY_PERMISSIONS_REQUEST_CALL_PHONE);
            return;
        }else{
            startActivity(callIntent);
        }

    }

    private void getAnIcuRoom
        (final String hospital_id,
         final String user_phone,
         final String user_id){
            progressDialog.show();

            Log.d("hospital_id",hospital_id);
            Log.d("user_id",user_id);
            Log.d("user_phone",user_phone);
            StringRequest request = new StringRequest(Request.Method.POST, URL.BASE_URL + URL.GET_ICU_ROOM_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("response",response);
                            try {
                                final JSONObject jsonObject= new JSONObject(response);
                                Log.d("jsonObject",jsonObject.toString());
                                if(jsonObject.getString("code").equals("0")){

                                    if(remedyHelper.getValueOfKey("app_language").equals("en")) {
                                        Toast.makeText(activity, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                                    }else {
                                        Toast.makeText(activity, jsonObject.getString("message_ar"), Toast.LENGTH_LONG).show();
                                    }
                                    new AlertDialog.Builder(activity)
                                            .setIcon(R.drawable.ic_attention)
                                            .setTitle(R.string.icu_attintion_title)
                                            .setMessage(activity.getResources().getString(R.string.icu_roul))
                                            .setPositiveButton(activity.getResources().getString(R.string.ok),new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    goToMap();
                                                }
                                            }).show();
                                }else {
                                    Toast.makeText(activity,jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.d("JSONException",e.toString());
                                Toast.makeText(activity,e.toString(),Toast.LENGTH_LONG).show();
                            }
                            progressDialog.dismiss();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(activity,R.string.display_failed, Toast.LENGTH_LONG).show();
                    Snackbar.make(mRoot,R.string.no_internet_connection,Snackbar.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> parameters = new HashMap<String, String>();

                    Log.d("hospital_id",hospital_id);
                    Log.d("user_phone",user_phone);
                    Log.d("user_id",user_id);
                    Log.d("date","");

                    parameters.put("hospital_id",hospital_id);
                    parameters.put("user_phone",user_phone);
                    parameters.put("user_id",user_id);
                    parameters.put("date","2019-07-30");

                    return parameters;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(activity);
            requestQueue.add(request);
        }

    public void goToDisplayDocFromHospitalActivity(String name,String phone,String address, Float price){

        Bundle bundle=new Bundle();
        bundle.putFloat("PRICE_KEY",price);
        bundle.putString("NAME_KEY",name);
        bundle.putString("PHONE_KEY",phone);
        bundle.putString("ADDRESS_KEY",address);
        Intent intent = new Intent (getApplicationContext(), DisplayDocFromHospitalActivity.class);
        intent.putExtras(bundle);

        startActivity(intent);
    }
    public void goToAppointmentActivity( int id, String name,String phone,String address, Float price){

        Bundle bundle=new Bundle();
        bundle.putFloat("PRICE_KEY",price);
        bundle.putInt("ID_KEY",id);
        bundle.putString("NAME_KEY",name);
        bundle.putString("PHONE_KEY",phone);
        bundle.putString("ADDRESS_KEY",address);
        Intent intent = new Intent (getApplicationContext(), GetAnAppointmentActivity.class);
        intent.putExtras(bundle);

        startActivity(intent);
    }


    public void goToMap(View v){
        try {
            //15.531608,32.553793
            String _lat = "15.531608";//location.split(",")[0];
            String _long = "32.553793";//location.split(",")[1];
            String label = getString(R.string.app_name);
            Uri gmmIntentUri = Uri.parse("geo:"+_lat+","+_long);
            Intent mapIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(gmmIntentUri+"?q="+ Uri.encode( _lat+","+_long+"("+label+")")));
            mapIntent.setPackage("com.google.android.apps.maps");
            if (mapIntent.resolveActivity(activity.getPackageManager()) != null) {
                activity.startActivity(mapIntent);
            }else{
                Toast.makeText(getBaseContext(),"Not supported",Toast.LENGTH_LONG).show();
            }
        }catch (Exception e){
            Toast.makeText(getBaseContext(),e.toString(),Toast.LENGTH_LONG).show();
        }
    }

    public void goToMap(){
        try {
            //15.531608,32.553793
            String _lat = "15.531608";//location.split(",")[0];
            String _long = "32.553793";//location.split(",")[1];
            String label = getString(R.string.app_name);
            Uri gmmIntentUri = Uri.parse("geo:"+_lat+","+_long);
            Intent mapIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(gmmIntentUri+"?q="+ Uri.encode( _lat+","+_long+"("+label+")")));
            mapIntent.setPackage("com.google.android.apps.maps");
            if (mapIntent.resolveActivity(activity.getPackageManager()) != null) {
                activity.startActivity(mapIntent);
            }else{
                Toast.makeText(getBaseContext(),"Not supported",Toast.LENGTH_LONG).show();
            }
        }catch (Exception e){
            Toast.makeText(getBaseContext(),e.toString(),Toast.LENGTH_LONG).show();
        }
    }
}
