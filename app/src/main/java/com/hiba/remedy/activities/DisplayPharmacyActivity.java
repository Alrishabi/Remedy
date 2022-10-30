package com.hiba.remedy.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hiba.remedy.R;
import com.hiba.remedy.URLs.URL;
import com.hiba.remedy.adapters.helper.HospitalAdapter;
import com.hiba.remedy.adapters.helper.HospitalList;
import com.hiba.remedy.adapters.helper.PharmacyAdapter;
import com.hiba.remedy.adapters.helper.PharmacyList;
import com.hiba.remedy.helpers.RemedyHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DisplayPharmacyActivity extends AppCompatActivity {
    private RecyclerView recyclerPharmacy;
    private ArrayList<PharmacyList> pharmacyLists = new ArrayList<PharmacyList>();
    PharmacyAdapter mAdapter;
    RemedyHelper remedyHelper;
    Activity activity;
    SearchView.OnQueryTextListener b;
    SwipeRefreshLayout swipeRefreshLayout;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_pharmacy);
        initeContext();
        initiateLayout();
    }

    public void initeContext() {
        activity = this;
        this.remedyHelper=new RemedyHelper(activity);
        progressDialog = new ProgressDialog(activity);
    }
    public  void initiateLayout(){
//        setHasOptionsMenu(true);
        setTitle(R.string.pharmacy);
        recyclerPharmacy = (RecyclerView)findViewById(R.id.recyclerView);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);


        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setRecyclerPharmacy();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        mAdapter = new PharmacyAdapter(pharmacyLists,getApplicationContext(),getSupportFragmentManager());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        recyclerPharmacy.setLayoutManager(mLayoutManager);
        recyclerPharmacy.setItemAnimator(new DefaultItemAnimator());
        recyclerPharmacy.setAdapter(mAdapter);

        progressDialog.setMessage(getResources().getString(R.string.loading_progress_dialog_message));

       setRecyclerPharmacy();

    }

    public void setRecyclerPharmacy() {

        pharmacyLists.clear();
//        swipeRefreshLayout.setRefreshing(true);
        progressDialog.show();

        StringRequest stringRequest =new StringRequest(Request.Method.GET,
                URL.BASE_URL+ URL.DISPLAY_PHARMACIES_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            Log.d("Response", s);
                            JSONObject jsonObject= new JSONObject(s);
                            JSONArray array = jsonObject.getJSONArray("pharmacy_table");
                            for(int i=0;i<array.length();i++){
                                JSONObject o = array.getJSONObject(i);
                                PharmacyList item= new PharmacyList(
                                        o.getInt("pharmacy_id"),
                                        o.getString("pharmacy_name"),
                                        o.getString("the_state"),
                                        o.getString(  "pharmacy_image"),
                                        o.getString("pharmacy_phone"),
                                        o.getString("pharmacy_address"),
                                        o.getString("pharmacy_location"),
                                        o.getLong("pharmacy_rate"),
                                        o.getInt("hospital_id"));

//                                {"hospital_id":"1",
//                                        "hospital_name":"TEST",
//                                        "hospital_image":"TEST",
//                                        "hospital_specialization":"TEST",
//                                        "hospital_phone":"3899792",
//                                        "hospital_address":"TEST",
//                                        "hospital_location":" VKCEVFK V",
//                                        "hospital_rate":"5",
//                                        "doctor_id":"1"},
                                pharmacyLists.add(item);
                                progressDialog.dismiss();

                            }
                            mAdapter =new PharmacyAdapter(pharmacyLists,getApplicationContext(),getSupportFragmentManager());
                            recyclerPharmacy.setAdapter(mAdapter);
                            progressDialog.dismiss();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("JSONException", e.toString());
                            progressDialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),R.string.connection_failed,Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                });
        RequestQueue requestQueue= Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }
//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.menu_items, menu);
//        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
//        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
//        searchView.setOnQueryTextListener(this);
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(activity.getComponentName()));
//        super.onCreateOptionsMenu(menu,inflater);
//    }
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
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == R.id.action_logout) {
//            logOut();
//            return true;
//        }else if(item.getItemId() == R.id.action_filter){
//            startActivityForResult(new Intent(getActivity(), FilterActivity.class),1);
//        }
//        return super.onOptionsItemSelected(item);
//    }

//    public void filterBySpecialState(String special, String state) {
//        if(!special.equals("-تخصص-") && state.equals("-محليه-")){
//            try {
//                if (special == null) {
//                    Toast.makeText(getContext(),R.string.not_item_found, Toast.LENGTH_LONG).show();
//                } else {
////              newText = newText.toLowerCase();
//                    ArrayList<HospitalList> newList = new ArrayList<>();
//                    for (HospitalList listItem : hosplist) {
//                        String name = listItem.getHospital_specialization().toLowerCase();
//                        if (name.contains(special)) {
//                            newList.add(listItem);
//                        }
//                    }
//                    mAdapter.setFilter(newList);
//                }
//                Toast.makeText(activity,"filter by: "+special, Toast.LENGTH_LONG).show();
//            }catch (Exception e){
//                Log.d( "Exception: ",e.toString());
//                Log.d( "nothing_to_search_it: ","nothing");
//                Toast.makeText(activity,R.string.nothing_to_search_it, Toast.LENGTH_LONG).show();
//            }
//        }else if(special.equals("-تخصص-") && !state.equals("-محليه-")){
//            try {
//                if (state == null) {
//                    Toast.makeText(getContext(),R.string.not_item_found, Toast.LENGTH_LONG).show();
//                } else {
////              newText = newText.toLowerCase();
//                    ArrayList<HospitalList> newList = new ArrayList<>();
//                    for (HospitalList listItem : hosplist) {
//                        String theState = listItem.getThe_state().toLowerCase();
//                        if (theState.contains(state)) {
//                            newList.add(listItem);
//                        }
//                    }
//                    mAdapter.setFilter(newList);
//                }
//                Toast.makeText(activity,"filter by: "+state, Toast.LENGTH_LONG).show();
//            }catch (Exception e){
//                Log.d( "Exception: ",e.toString());
//                Log.d( "nothing_to_search_it: ","nothing");
//                Toast.makeText(activity,R.string.nothing_to_search_it, Toast.LENGTH_LONG).show();
//            }
//        }else if(!special.equals("-تخصص-") && !state.equals("-محليه-")){
//            try {
//                if (state == null && special==null) {
//                    Toast.makeText(getContext(),R.string.not_item_found, Toast.LENGTH_LONG).show();
//                } else {
////              newText = newText.toLowerCase();
//                    ArrayList<HospitalList> newList = new ArrayList<>();
//                    for (HospitalList listItem : hosplist) {
//                        String theState = listItem.getThe_state().toLowerCase();
//                        String spical = listItem.getHospital_specialization().toLowerCase();
//                        if (theState.contains(state) && spical.contains(special) ) {
//                            newList.add(listItem);
//                        }
//                    }
//                    mAdapter.setFilter(newList);
//                }
//                Toast.makeText(activity,"filter by: "+state+" and "+special, Toast.LENGTH_LONG).show();
//            }catch (Exception e){
//                Log.d( "Exception: ",e.toString());
//                Log.d( "nothing_to_search_it: ","nothing");
//                Toast.makeText(activity,R.string.nothing_to_search_it, Toast.LENGTH_LONG).show();
//            }
//        }
//    }
//    @Override
//    public boolean onQueryTextSubmit(String query) {
//        return true;
//    }
//    @Override
//    public boolean onQueryTextChange(String newText) {
//        try {
//            if (newText == null) {
//                Toast.makeText(getContext(),R.string.not_item_found, Toast.LENGTH_LONG).show();
//            } else {
//                newText = newText.toLowerCase();
//                ArrayList<HospitalList> newList = new ArrayList<>();
//                for (HospitalList listItem : hosplist) {
//                    String name = listItem.getHospital_name().toLowerCase();
//                    if (name.contains(newText)) {
//                        newList.add(listItem);
//                    }
//                }mAdapter.setFilter(newList);
//            }
//        }catch (Exception e){
//            Toast.makeText(getContext(),R.string.nothing_to_search_it, Toast.LENGTH_LONG).show();
//        }
//        return true;
//    }
}
