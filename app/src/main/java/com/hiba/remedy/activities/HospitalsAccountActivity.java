package com.hiba.remedy.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import com.hiba.remedy.R;
import com.hiba.remedy.fragments.DoctorsHospitalFragment;
import com.hiba.remedy.fragments.HospitalReportsFragment;
import com.hiba.remedy.fragments.MyProfileFragment;
import com.hiba.remedy.helpers.RemedyHelper;

public class HospitalsAccountActivity extends AppCompatActivity {
    RemedyHelper remedyHelper;
    Activity activity;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospitals);
        initContext();
        initeLayout();
    }
    public void initContext() {
        activity = this;
        this.remedyHelper=new RemedyHelper(activity);
        progressDialog = new ProgressDialog(activity);
    }
    public void initeLayout(){
//        getActionBar().show();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(remedyHelper.getValueOfKey("app_language").equals("en")){
            setTitle(getResources().getString(R.string.title_hospitals)+" "+getResources().getString(R.string.account));

        }else {
            setTitle(getResources().getString(R.string.account)+" "+getResources().getString(R.string.title_hospitals));

        }

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
//        setActionBar(toolbar);
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = findViewById(R.id.fab);
//        activity.setTitle("test");
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                logOut();
            }
        });
    }
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private SectionsPagerAdapter(FragmentManager fm)
        {
            super(fm);
        }
        @Override
        public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return new DoctorsHospitalFragment(activity);
                    case 1:
                        return new HospitalReportsFragment(activity);
                    case 2:
                        return new MyProfileFragment(activity);
                    default:
                        return null;
                }

        }
        @Override
        public int getCount() {
            return 3;
        }
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getResources().getString(R.string.title_doctors);
                case 1:
                    return getResources().getString(R.string.title_report);
                case 2:
                    return getResources().getString(R.string.title_my_profile);
            }
            return null;
        }
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

}