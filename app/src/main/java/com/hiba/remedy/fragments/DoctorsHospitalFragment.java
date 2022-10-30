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
import com.hiba.remedy.activities.FilterActivity;
import com.hiba.remedy.activities.LoginActivity;
import com.hiba.remedy.adapters.helper.DisDoctorAdapter;
import com.hiba.remedy.adapters.helper.DoctorsAdapter;
import com.hiba.remedy.adapters.helper.DoctorsList;
import com.hiba.remedy.helpers.RemedyHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@SuppressLint("ValidFragment")
public class DoctorsHospitalFragment extends Fragment implements SearchView.OnQueryTextListener{

    private RecyclerView recyclerview;
    private ArrayList<DoctorsList> doctorsLists = new ArrayList<DoctorsList>();
    DisDoctorAdapter mAdapter;
    SwipeRefreshLayout swipeRefreshLayout;
    ProgressDialog progressDialog;
    RemedyHelper remedyHelper;
    Activity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView= inflater.inflate(R.layout.fragment_doctors, container, false);

        initiateLayout(rootView);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setRecyclerDoctor();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        setRecyclerDoctor();

        return rootView;
    }

    @SuppressLint("ValidFragment")
    public DoctorsHospitalFragment(Activity activity) {
        this.activity = activity;
        remedyHelper = new RemedyHelper(activity);
        progressDialog = new ProgressDialog(activity);
    }

    public  void initiateLayout(View view){
        setHasOptionsMenu(true);
//        getActivity().setTitle(R.string.title_doctors);

        recyclerview = (RecyclerView) view.findViewById(R.id.recyclerView);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.hospi_swap);

        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        mAdapter = new DisDoctorAdapter(doctorsLists,getContext(),getFragmentManager(),activity);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerview.setLayoutManager(mLayoutManager);
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.setAdapter(mAdapter);
        progressDialog.setMessage(getResources().getString(R.string.loading_progress_dialog_message));


    }

    public void setRecyclerDoctor() {
        progressDialog.dismiss();
        doctorsLists.clear();
        StringRequest stringRequest =new StringRequest(Request.Method.POST,
                URL.BASE_URL+URL.DISPLAY_DOCTORS_HOSPITAL_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            Log.d("Response", s);
                            JSONObject jsonObject= new JSONObject(s);
                            JSONArray array = jsonObject.getJSONArray("doctors_table");
                            for(int i=0;i<array.length();i++){
                                JSONObject o = array.getJSONObject(i);
                                DoctorsList item= new DoctorsList(
                                        o.getInt("doctor_id"),
                                        o.getString("doctor_name"),
                                        o.getString(  "doctor_image"),
                                        o.getString("doctor_specialization"),
                                        o.getString("the_state"),
                                        o.getString("doctor_phone"),
                                        o.getString("doctor_Workplace"),
                                        o.getLong("appointment_price"));

                                doctorsLists.add(item);
                            }
                            mAdapter =new DisDoctorAdapter(doctorsLists,getContext(),getFragmentManager(),activity);
                            recyclerview.setAdapter(mAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("JSONException", e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(activity,R.string.connection_failed,Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();

                parameters.put("user_id",remedyHelper.getValueOfKey("user_id"));

                return parameters;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
        progressDialog.dismiss();
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getActivity();
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            filterBySpecialState(data.getStringExtra( "result"),data.getStringExtra( "state"));
            // data.getStringExtra( "result");
            //some code
        }
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        try {
            if (newText.equals(null)) {
                Toast.makeText(getContext(),R.string.not_item_found, Toast.LENGTH_LONG).show();
            } else {
                newText = newText.toLowerCase();
                ArrayList<DoctorsList> newList = new ArrayList<>();
                for (DoctorsList listItem : doctorsLists) {
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

    public void logOut() {

        remedyHelper.setValueToKey("is_login","0");
        remedyHelper.setValueToKey("user_id","");
        remedyHelper.setValueToKey("user_name","");
        remedyHelper.setValueToKey("user_phone","");
        remedyHelper.setValueToKey("security_qu_answer","");

        startActivity(new Intent(getActivity(), LoginActivity.class));

        activity.finish();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            logOut();
            return true;
        }else if(item.getItemId() == R.id.action_filter){
            if (item.getItemId() == R.id.action_logout) {
                logOut();
                return true;
            }else if(item.getItemId() == R.id.action_filter){
                startActivityForResult(new Intent(getActivity(), FilterActivity.class),1);
            }
            return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    public void filterBySpecialState(String special, String state) {
        if(!special.equals("-تخصص-") && state.equals("-محليه-")){
            try {
                if (special == null) {
                    Toast.makeText(getContext(),R.string.not_item_found, Toast.LENGTH_LONG).show();
                } else {
//              newText = newText.toLowerCase();
                    ArrayList<DoctorsList> newList = new ArrayList<>();
                    for (DoctorsList listItem : doctorsLists) {
                        String name = listItem.getDoctor_specialization().toLowerCase();
                        if (name.contains(special)) {
                            newList.add(listItem);
                        }
                    }
                    mAdapter.setFilter(newList);
                }
                Toast.makeText(activity,"filter by: "+special, Toast.LENGTH_LONG).show();
            }catch (Exception e){
                Log.d( "Exception: ",e.toString());
                Log.d( "nothing_to_search_it: ","nothing");
                Toast.makeText(activity,R.string.nothing_to_search_it, Toast.LENGTH_LONG).show();
            }
        }else if(special.equals("-تخصص-") && !state.equals("-محليه-")){
            try {
                if (state == null) {
                    Toast.makeText(getContext(),R.string.not_item_found, Toast.LENGTH_LONG).show();
                } else {
//              newText = newText.toLowerCase();
                    ArrayList<DoctorsList> newList = new ArrayList<>();
                    for (DoctorsList listItem : doctorsLists) {
                        String theState = listItem.getThe_state().toLowerCase();
                        if (theState.contains(state)) {
                            newList.add(listItem);
                        }
                    }
                    mAdapter.setFilter(newList);
                }
                Toast.makeText(activity,"filter by: "+state, Toast.LENGTH_LONG).show();
            }catch (Exception e){
                Log.d( "Exception: ",e.toString());
                Log.d( "nothing_to_search_it: ","nothing");
                Toast.makeText(activity,R.string.nothing_to_search_it, Toast.LENGTH_LONG).show();
            }
        }else if(!special.equals("-تخصص-") && !state.equals("-محليه-")){
            try {
                if (state == null && special==null) {
                    Toast.makeText(getContext(),R.string.not_item_found, Toast.LENGTH_LONG).show();
                } else {
//              newText = newText.toLowerCase();
                    ArrayList<DoctorsList> newList = new ArrayList<>();
                    for (DoctorsList listItem : doctorsLists) {
                        String theState = listItem.getThe_state().toLowerCase();
                        String spical = listItem.getDoctor_specialization().toLowerCase();
                        if (theState.contains(state) && spical.contains(special) ) {
                            newList.add(listItem);
                        }
                    }
                    mAdapter.setFilter(newList);
                }
                Toast.makeText(activity,"filter by: "+state+" and "+special, Toast.LENGTH_LONG).show();
            }catch (Exception e){
                Log.d( "Exception: ",e.toString());
                Log.d( "nothing_to_search_it: ","nothing");
                Toast.makeText(activity,R.string.nothing_to_search_it, Toast.LENGTH_LONG).show();
            }
        }
    }

//    private void filterBySpeical(String newText) {
//        try {
//            if (newText.equals(null)) {
//                Toast.makeText(getContext(),R.string.not_item_found, Toast.LENGTH_LONG).show();
//            } else {
////              newText = newText.toLowerCase();
//                ArrayList<DoctorsList> newList = new ArrayList<>();
//                for (DoctorsList listItem : doctorsLists) {
//                    String name = listItem.getDoctor_specialization().toLowerCase();
//                    if (name.contains(newText)) {
//                        newList.add(listItem);
//                    }
//                }
//                mAdapter.setFilter(newList);
//            }
//            Toast.makeText(getContext(),"filter by: "+newText, Toast.LENGTH_LONG).show();
//        }catch (Exception e){
//            Toast.makeText(getContext(),R.string.nothing_to_search_it, Toast.LENGTH_LONG).show();
//        }
//    }
}
