package com.hiba.remedy.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.view.View;

import com.hiba.remedy.R;
import com.hiba.remedy.fragments.DoctorsFragment;
import com.hiba.remedy.fragments.HospitalsFragment;
import com.hiba.remedy.fragments.LabsFragment;
import com.hiba.remedy.fragments.MyProfilePatientAccountFragment;
import com.hiba.remedy.fragments.PharmacistFragment;
import com.hiba.remedy.helpers.RemedyHelper;

public class HomeActivity extends AppCompatActivity {
    Activity activity;
  RemedyHelper remedyHelper;
  ProgressDialog progressDialog;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_hospitals:
                    fragment = new HospitalsFragment(activity);
                    break;
                case R.id.navigation_doctors:
                    fragment = new DoctorsFragment(activity);
                    break;
                case R.id.navigation_Laps:
                    fragment = new LabsFragment(activity);
                    break;
                case R.id.navigation_pharmacy:
                    fragment = new PharmacistFragment(activity);
                    break;
                case R.id.navigation_my_profile:
                    fragment = new MyProfilePatientAccountFragment(activity);
                    break;
            }
            return loadFragment(fragment);
        }
    };

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initeContext();
        initLayout();

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }



    public void logOut() {
        remedyHelper.setValueToKey("is_login","0");
        remedyHelper.setValueToKey("user_id","");
        remedyHelper.setValueToKey("user_name","");
        remedyHelper.setValueToKey("user_phone","");
        remedyHelper.setValueToKey("security_qu_answer","");
        remedyHelper.setValueToKey("user_category","");
        startActivity(new Intent(activity, LoginActivity.class));
        activity.finish();
    }

    public void initeContext() {
        activity = this;
        this.remedyHelper=new RemedyHelper(activity);
        progressDialog = new ProgressDialog(activity);
    }

    public void initLayout() {
        if(remedyHelper.getValueOfKey("app_language").equals("en")){
            setTitle(getResources().getString(R.string.title_patient)+" "+getResources().getString(R.string.account));
        }else {
            setTitle(getResources().getString(R.string.account) + " " + getResources().getString(R.string.title_patient));
        }
        BottomNavigationView navView = findViewById(R.id.nav_view);
        loadFragment(new HospitalsFragment(activity));

        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                logOut();
            }
        });
    }
}
