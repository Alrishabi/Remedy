package com.hiba.remedy.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.hiba.remedy.R;
import com.hiba.remedy.URLs.URL;
import com.hiba.remedy.fragments.HospitalsFragment;
import com.hiba.remedy.helpers.RemedyHelper;

import java.util.ArrayList;
import java.util.List;

public class FilterActivity extends AppCompatActivity {


    Button b_filter;
    RemedyHelper remedyHelper;
    Activity activity;
    URL url;
    LinearLayout mRoot;
    public HospitalsFragment hospitalsFragment;
    ProgressDialog progressDialog;
    Spinner sp_spical,sp_state;
    List<String> spList;
    List<String> stateList;
    LinearLayout LL_spcial;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        initiateContext();
        initiateLayout();
    }
    private void initiateContext(){
        activity              = this;
        remedyHelper          = new RemedyHelper(activity);
        progressDialog        = new ProgressDialog(activity);
        hospitalsFragment = new HospitalsFragment(activity);
        spList = new ArrayList<String>();
        stateList = new ArrayList<String>();
    }
    public  void initiateLayout(){
        spList.add("-تخصص-");
        spList.add("أطفال");
        spList.add("عظام");
        spList.add("جراحة");
        spList.add("أسنان");
        spList.add("نساء وتوليد");
        spList.add("باطنية");
        spList.add("مخ واعصاب");
        spList.add("جلديه");

        stateList.add("-محليه-");
        stateList.add("الخرطوم");
        stateList.add("بحري");
        stateList.add("امدرمان");

        LL_spcial                =  findViewById(R.id.LL_spcial);
        sp_spical                = (Spinner) findViewById(R.id.sp_spical);
        sp_state                 = (Spinner) findViewById(R.id.sp_state);
        b_filter                 = (Button) findViewById(R.id.b_filter);

        LL_spcial.setVisibility(View.VISIBLE);

        progressDialog.setMessage(getResources().getString(R.string.logging_progress_message));

        setTitle(getString(R.string.filter));

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spList);
        ArrayAdapter<String> stateAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, stateList);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        sp_spical.setAdapter(dataAdapter);
        sp_state.setAdapter(stateAdapter);
        // if(et_password.setTransformationMethod(new PasswordTransformationMethod())){}


        b_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d( "selecteditem: ",sp_spical.getSelectedItem().toString());
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result", sp_spical.getSelectedItem().toString());
                returnIntent.putExtra("state", sp_state.getSelectedItem().toString());

                setResult(Activity.RESULT_OK,returnIntent);
                finish();
//                hospitalsFragment.filterBySpeical(sp_spical.getSelectedItem().toString());
////                String id=et_id.getText().toString().trim();
////                String pass=et_password.getText().toString().trim();
//                finish();
//                if(validation(id,pass)){
//                    // clearForm();
//                    loginMethod(id,pass);
//                LoginFun();
//                }

            }
        });
    }
}
