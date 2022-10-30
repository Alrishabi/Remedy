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
import android.view.View;
import com.hiba.remedy.R;
import com.hiba.remedy.fragments.DoctorAppointmentFragment;
import com.hiba.remedy.fragments.DoctorReportsFragment;
import com.hiba.remedy.fragments.MyPatientsFragment;
import com.hiba.remedy.fragments.MyProfileFragment;
import com.hiba.remedy.fragments.TakePatientFragment;
import com.hiba.remedy.helpers.RemedyHelper;

public class DoctorsActivity extends AppCompatActivity {
RemedyHelper remedyHelper;
Activity activity;
ProgressDialog progressDialog;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private int[] tabIcons = {
            R.drawable.ic_tabe_speaker,
            R.drawable.ic_my_broadcast_tabe_icon,
            R.drawable.ic_vote_tabe_icon,
            R.drawable.ic_qution_tabe_icon,
            R.drawable.your_info_tabe_icon
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors);
        initeContext();
        initateLayout();

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    public void initeContext() {
        activity = this;
        this.remedyHelper=new RemedyHelper(activity);
        progressDialog = new ProgressDialog(activity);
    }
public void initateLayout(){
//    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//    setSupportActionBar(toolbar);
    setTitle(getResources().getString(R.string.account)+" "+getResources().getString(R.string.doctor));

    DoctorsActivity.SectionsPagerAdapter sectionsPagerAdapter = new DoctorsActivity.SectionsPagerAdapter(getSupportFragmentManager());
    ViewPager viewPager = findViewById(R.id.view_pager);
    viewPager.setAdapter(sectionsPagerAdapter);
    TabLayout tabs = findViewById(R.id.tabs);

    TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
    tabLayout.setupWithViewPager(viewPager);
    tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
//    tabLayout.getTabAt(0).setIcon(tabIcons[0]);
//    tabLayout.getTabAt(1).setIcon(tabIcons[1]);
//    tabLayout.getTabAt(2).setIcon(tabIcons[2]);
//    tabLayout.getTabAt(3).setIcon(tabIcons[3]);
//    tabLayout.getTabAt(4).setIcon(tabIcons[4]);

    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
            logOut();
        }
    });
}
    /**
     * A placeholder fragment containing a simple view.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case 0:
                    return new TakePatientFragment(activity);
                case 1:
                    return new DoctorAppointmentFragment(activity);
                case 2:
                    return new MyPatientsFragment(activity);
                case 3:
                    return new DoctorReportsFragment(activity);
                case 4:
                    return new MyProfileFragment(activity);
                default:
                    return null;
            }
//            return null;
        }

        @Override
        public int getCount() {
            // Show 5 total pages.
            return 5;
        }
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getResources().getString(R.string.check_patient);
                case 1:
                    return getResources().getString(R.string.my_appointments);
                case 2:
                    return getResources().getString(R.string.patients);
                case 3:
                    return getResources().getString(R.string.title_report);
                case 4:
                    return getResources().getString(R.string.title_my_profile);
            }
            return null;
        }
    }
    public void logOut() {

        remedyHelper.setValueToKey("is_login", "0");
        remedyHelper.setValueToKey("user_id", "");
        remedyHelper.setValueToKey("user_name", "");
        remedyHelper.setValueToKey("user_phone", "");
        remedyHelper.setValueToKey("security_qu_answer", "");
        remedyHelper.setValueToKey("user_category", "");

        startActivity(new Intent(activity, LoginActivity.class));

        activity.finish();
    }
}

