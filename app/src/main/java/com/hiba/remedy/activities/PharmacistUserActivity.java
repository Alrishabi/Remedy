package com.hiba.remedy.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.hiba.remedy.R;
import com.hiba.remedy.fragments.MyProfileFragment;
import com.hiba.remedy.fragments.TakePatientFragment;
import com.hiba.remedy.helpers.RemedyHelper;

public class PharmacistUserActivity extends AppCompatActivity {
    RemedyHelper remedyHelper;
    Activity activity;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacist_user);
        initeContext();
        initLayout();
    }
    public void initeContext() {
        activity = this;
        this.remedyHelper=new RemedyHelper(activity);
        progressDialog = new ProgressDialog(activity);
    }
    public void initLayout() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getResources().getString(R.string.account)+" "+getResources().getString(R.string.pharmacist));

      SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
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
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private final Context mContext;

        public SectionsPagerAdapter(Context context, FragmentManager fm) {
            super(fm);
            mContext = context;
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case 0:
                    return new TakePatientFragment(activity);
                case 1:
                    return new MyProfileFragment(activity);
                default:
                    return null;
            }
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getResources().getString(R.string.take_patient);
                case 1:
                    return getResources().getString(R.string.title_my_profile);
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }
    }
}