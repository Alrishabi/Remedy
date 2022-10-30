package com.hiba.remedy.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.hiba.remedy.R;
import com.hiba.remedy.activities.PatientAppointmentActivity;
import com.hiba.remedy.activities.PatientReportActivity;
import com.hiba.remedy.helpers.RemedyHelper;

@SuppressLint("ValidFragment")
public class MyProfilePatientAccountFragment extends Fragment {
    Activity activity;
    RemedyHelper remedyHelper;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView= inflater.inflate(R.layout.fragment_my_profile, container, false);
        intiateLayout(rootView);

        /**
         * Showing Swipe Refresh animation on activity create
         * As animation won't start on onCreate, post runnable is used
         */
        return rootView;
    }
    private void intiateLayout(View view){
        setHasOptionsMenu(true);
        EditText ed_name = view.findViewById(R.id.ed_name);
        EditText ed_user_id = view.findViewById(R.id.ed_id);
        ed_name.setText(remedyHelper.getValueOfKey("user_full_name"));
        ed_user_id.setText(remedyHelper.getValueOfKey("user_id"));

    }
    public MyProfilePatientAccountFragment(Activity activity) {
        this.activity = activity;
        this.remedyHelper=new RemedyHelper(activity);
        progressDialog = new ProgressDialog(activity);
    }
@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_patient_account, menu);

        super.onCreateOptionsMenu(menu,inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_appointments) {
        startActivity(new Intent(getActivity(), PatientAppointmentActivity.class));
        return true;
        }else if (item.getItemId() == R.id.action_reports) {
            startActivity(new Intent(getActivity(), PatientReportActivity.class));
        return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
