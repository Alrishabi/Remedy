package com.hiba.remedy.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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
import com.hiba.remedy.adapters.helper.AppointmentAdapter;
import com.hiba.remedy.adapters.helper.AppointmentList;
import com.hiba.remedy.adapters.helper.PatientList;
import com.hiba.remedy.helpers.RemedyHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PatientAppointmentActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{
    private static RecyclerView recyclerview;
    private static ArrayList<AppointmentList> appointmentLists = new ArrayList<AppointmentList>();
    static AppointmentAdapter mAdapter;
    static Activity activity;
    static RemedyHelper remedyHelper;
    static ProgressDialog progressDialog;
    SwipeRefreshLayout swipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_appointments);
        initeContext();
        initiateLayout();
    }

    public void initeContext() {
        activity = this;
        this.remedyHelper=new RemedyHelper(activity);
        progressDialog = new ProgressDialog(activity);
    }

    public  void initiateLayout(){
       setTitle(R.string.my_appointments);
        mAdapter = new AppointmentAdapter(appointmentLists,getApplicationContext(),getSupportFragmentManager());

        recyclerview = (RecyclerView) findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        recyclerview.setLayoutManager(mLayoutManager);
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.setAdapter(mAdapter);
        progressDialog.setMessage(getResources().getString(R.string.loading_progress_dialog_message));
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        setRecyclerviewAppointment();
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setRecyclerviewAppointment();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

    }
    public void setRecyclerviewAppointment() {
        appointmentLists.clear();
        progressDialog.show();
        StringRequest stringRequest =new StringRequest(Request.Method.POST,
                URL.BASE_URL+URL.DISPLAY_APPOINTMENT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            Log.d("Response", s);
                            JSONObject jsonObject= new JSONObject(s);
                            JSONArray array = jsonObject.getJSONArray("appointment_table");
                            for(int i=0;i<array.length();i++){
                                JSONObject o = array.getJSONObject(i);
                                AppointmentList item= new AppointmentList(
                                        o.getInt("appointment_id"),
                                        o.getString("patient_name"),
                                        o.getString("user_phone"),
                                        o.getLong("appointment_price"),
                                        o.getInt("doctor_id"),
                                        o.getString("doctor_name"),
                                        o.getString("doctor_phone"),
                                        o.getString("appointment_address"),
                                        o.getString("appointment_date"),
                                        o.getInt("user_id"),
                                        o.getString("hospital_id"));

                                appointmentLists.add(item);
                                progressDialog.dismiss();

                            }
                            mAdapter =new AppointmentAdapter(appointmentLists,activity,getSupportFragmentManager());
                            recyclerview.setAdapter(mAdapter);
                            progressDialog.dismiss();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("JSONExceptionA", e.toString());
                            progressDialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(activity,R.string.connection_failed,Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();

                parameters.put("user_id",remedyHelper.getValueOfKey("user_id"));
//                parameters.put("user_id","1");

                return parameters;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }
    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        try {
            if (newText == null) {
                Toast.makeText(activity,R.string.not_item_found, Toast.LENGTH_LONG).show();
            } else {
                newText = newText.toLowerCase();
                ArrayList<AppointmentList> newList = new ArrayList<>();
                for (AppointmentList listItem : appointmentLists) {
                    String name = listItem.getPatient_name().toLowerCase();
                    if (name.contains(newText)) {
                        newList.add(listItem);
                    }
                }mAdapter.setFilter(newList);
            }
        }catch (Exception e){
            Toast.makeText(activity,R.string.nothing_to_search_it, Toast.LENGTH_LONG).show();
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_patient, menu);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) activity.getSystemService(Context.SEARCH_SERVICE);
        searchView.setOnQueryTextListener(this);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(activity.getComponentName()));
        super.onCreateOptionsMenu(menu);
        return true;
    }
}
