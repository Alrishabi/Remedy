package com.hiba.remedy.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import com.hiba.remedy.R;
import com.hiba.remedy.helpers.RemedyHelper;

import java.util.Locale;

public class SplashActivity extends AppCompatActivity {

RemedyHelper remedyHelper;
Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        remedyHelper          = new RemedyHelper(activity);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Thread logo_screen = new Thread() {

            public void run() {
                try {
                    sleep(5100);

                    if(remedyHelper.getValueOfKey("is_login").equals("1")){
                        if(remedyHelper.getValueOfKey("user_category").equals("1"))
                        {
                            Intent i = new Intent(activity,HomeActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                        }
                        else if(remedyHelper.getValueOfKey("user_category").equals("2"))
                        {
                            Intent i = new Intent(activity, HospitalsAccountActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                        }else if(remedyHelper.getValueOfKey("user_category").equals("3"))
                        {
                            Intent i = new Intent(activity,DoctorsActivity.class);
                             i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);

                        }else if(remedyHelper.getValueOfKey("user_category").equals("4"))
                        {
                            Intent i = new Intent(activity,LaboratoryUserActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);

                        }else if(remedyHelper.getValueOfKey("user_category").equals("5"))
                        {
                            Intent i = new Intent(activity,PharmacistUserActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                        }
                    }else{
                         Intent i = new Intent(getApplicationContext(),LoginActivity.class);
                        startActivity(i);
                    }
//                        Intent i = new Intent(getApplicationContext(),DisplayPharmacyActivity.class);
//                        startActivity(i);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    finish();
                }
            }
        };
        logo_screen.start();

        if(remedyHelper.getValueOfKey("app_language").equals("en")){
            remedyHelper.setValueToKey("app_language","en");
            Locale locale = new Locale("en");
            remedyHelper.log("APP_LANGUAGE_IF",remedyHelper.getValueOfKey("app_language"));
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
            // Toast.makeText(this, getResources().getString(R.string.lbl_langSelectEnglis), Toast.LENGTH_SHORT).show();
        }else{
            remedyHelper.setValueToKey("app_language","ar");
            Locale locale = new Locale("ar");
            remedyHelper.log("APP_LANGUAGE_ELSE",remedyHelper.getValueOfKey("app_language"));
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
            //  Toast.makeText(this, getResources().getString(R.string.lbl_langSelecURdu), Toast.LENGTH_SHORT).show();
        }
    }


}
