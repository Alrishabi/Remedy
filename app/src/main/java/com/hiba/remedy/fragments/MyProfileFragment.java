package com.hiba.remedy.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.hiba.remedy.R;
import com.hiba.remedy.helpers.RemedyHelper;

@SuppressLint("ValidFragment")
public class MyProfileFragment extends Fragment {
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
        EditText ed_name = view.findViewById(R.id.ed_name);
        EditText ed_user_id = view.findViewById(R.id.ed_id);
        ed_name.setText(remedyHelper.getValueOfKey("user_full_name"));
        ed_user_id.setText(remedyHelper.getValueOfKey("user_id"));

    }
    public MyProfileFragment(Activity activity) {
        this.activity = activity;
        this.remedyHelper=new RemedyHelper(activity);
        progressDialog = new ProgressDialog(activity);
    }
}
