package com.hiba.remedy.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import com.hiba.remedy.activities.LoginActivity;
import com.hiba.remedy.adapters.helper.AppointmentAdapter;
import com.hiba.remedy.adapters.helper.AppointmentList;
import com.hiba.remedy.helpers.RemedyHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


@SuppressLint("ValidFragment")
public class DisplayPatientAppointmentsFragment extends Fragment implements SearchView.OnQueryTextListener {
    private RecyclerView recyclerview;
    private ArrayList<AppointmentList> appointmentLists = new ArrayList<AppointmentList>();
    AppointmentAdapter mAdapter;
    Activity activity;
    RemedyHelper remedyHelper;
    ProgressDialog progressDialog;
    SwipeRefreshLayout swipeRefreshLayout;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =inflater.inflate(R.layout.fragment_appointments, container, false);

        initiateLayout(rootView);
       setRecyclerviewAppointment();
        return rootView;
    }

    @SuppressLint("ValidFragment")
    public DisplayPatientAppointmentsFragment(Activity activity) {
        this.activity = activity;
        remedyHelper=new RemedyHelper(activity);
        progressDialog = new ProgressDialog(activity);
    }

    public  void initiateLayout(View view){
        setHasOptionsMenu(true);

        mAdapter = new AppointmentAdapter(appointmentLists,getContext(),getFragmentManager());

        recyclerview = (RecyclerView) view.findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerview.setLayoutManager(mLayoutManager);
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.setAdapter(mAdapter);
        progressDialog.setMessage(getResources().getString(R.string.loading_progress_dialog_message));
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);

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
                            mAdapter =new AppointmentAdapter(appointmentLists,getContext(),getFragmentManager());
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
                        Toast.makeText(getContext(),R.string.connection_failed,Toast.LENGTH_LONG).show();
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
        RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_items, menu);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView.setOnQueryTextListener(this);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        super.onCreateOptionsMenu(menu,inflater);
    }
    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        try {
            if (newText.equals(null)) {
                Toast.makeText(getContext(),R.string.not_item_found, Toast.LENGTH_LONG).show();
            } else {
                newText = newText.toLowerCase();
                ArrayList<AppointmentList> newList = new ArrayList<>();
                for (AppointmentList listItem : appointmentLists) {
                    String name = listItem.getDoctor_name().toLowerCase();
                    if (name.contains(newText)) {
                        newList.add(listItem);
                    }
                }mAdapter.setFilter(newList);
            }
        }catch (Exception e){
            Toast.makeText(getContext(),R.string.nothing_to_search_it, Toast.LENGTH_LONG).show();
        }
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            logOut();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void logOut() {

        remedyHelper.setValueToKey("is_login","0");
        remedyHelper.setValueToKey("user_id","");
        remedyHelper.setValueToKey("user_name","");
        remedyHelper.setValueToKey("user_phone","");
        remedyHelper.setValueToKey("security_qu_answer","");

        startActivity(new Intent(getActivity(), LoginActivity.class));

        activity.finish();
    }
}
