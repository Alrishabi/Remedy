package com.hiba.remedy.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.hiba.remedy.R;
import com.hiba.remedy.helpers.RemedyHelper;

public class MainActivity extends AppCompatActivity {
     Activity activity;
     RemedyHelper remedyHelper;
     ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initiateContext();
        initatLayout();

    }
    private void initiateContext(){
        activity        = this;
        remedyHelper          = new RemedyHelper(activity);
        progressDialog = new ProgressDialog(activity);
    }
    public void initatLayout(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });
    }

}
