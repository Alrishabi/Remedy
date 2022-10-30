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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hiba.remedy.R;
import com.hiba.remedy.URLs.URL;
import com.hiba.remedy.activities.FilterActivity;
import com.hiba.remedy.activities.FilterPharmacistAndLabsActivity;
import com.hiba.remedy.activities.LoginActivity;
import com.hiba.remedy.adapters.helper.HospitalList;
import com.hiba.remedy.adapters.helper.PharmacyAdapter;
import com.hiba.remedy.adapters.helper.PharmacyList;
import com.hiba.remedy.helpers.RemedyHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

@SuppressLint("ValidFragment")
public class PharmacistFragment extends Fragment implements SearchView.OnQueryTextListener {
    private RecyclerView recyclerPharmacy;
    private ArrayList<PharmacyList> pharmacyLists = new ArrayList<PharmacyList>();
    PharmacyAdapter mAdapter;
    RemedyHelper remedyHelper;
    Activity activity;
    SearchView.OnQueryTextListener b;
    SwipeRefreshLayout swipeRefreshLayout;
    ProgressDialog progressDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView= inflater.inflate(R.layout.activity_display_pharmacy, container, false);

        initiateLayout(rootView);

        setRecyclerPharmacy();
        // activity.setTitle(R.string.title_hospitals);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setRecyclerPharmacy();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        /**
         * Showing Swipe Refresh animation on activity create
         * As animation won't start on onCreate, post runnable is used
         */
        return rootView;
    }

    @SuppressLint("ValidFragment")
    public PharmacistFragment(Activity activity) {
        this.activity = activity;
        this.remedyHelper=new RemedyHelper(activity);
        progressDialog = new ProgressDialog(activity);
    }
    public  void initiateLayout(View view){
        setHasOptionsMenu(true);
//        setTitle(R.string.pharmacy);
        recyclerPharmacy = view.findViewById(R.id.recyclerView);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);


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

        mAdapter = new PharmacyAdapter(pharmacyLists,getActivity(),getFragmentManager());
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
                            mAdapter =new PharmacyAdapter(pharmacyLists,getContext(),getFragmentManager());
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
                        Toast.makeText(getActivity(),R.string.connection_failed,Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                });
        RequestQueue requestQueue= Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }
    public void filterBySpecialState( String state) {
        if(state.equals("-محليه-")){
            Toast.makeText(getContext(),getResources().getString(R.string.no_thing_select_it)+" "+getResources().getString(R.string.pharmacy), Toast.LENGTH_LONG).show();
        }else if( !state.equals("-محليه-")){
            try {
//              newText = newText.toLowerCase();
                    ArrayList<PharmacyList> newList = new ArrayList<>();
                    for (PharmacyList listItem : pharmacyLists) {
                        String theState = listItem.getThe_state().toLowerCase();
                        if (theState.contains(state)) {
                            newList.add(listItem);
                        }
                    }
                    mAdapter.setFilter(newList);

                Toast.makeText(activity,getResources().getString(R.string.filter_it)+" "+state, Toast.LENGTH_LONG).show();
            }catch (Exception e){
                Log.d( "Exception: ",e.toString());
                Log.d( "nothing_to_search_it: ","nothing");
            }
        }

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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            logOut();
            return true;
        }else if(item.getItemId() == R.id.action_filter){
            startActivityForResult(new Intent(getActivity(), FilterPharmacistAndLabsActivity.class),1);
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }
    @Override
    public boolean onQueryTextChange(String newText) {
        try {
            if (newText == null) {
                Toast.makeText(getContext(),R.string.not_item_found, Toast.LENGTH_LONG).show();
            } else {
                newText = newText.toLowerCase();
                ArrayList<PharmacyList> newList = new ArrayList<>();
                for (PharmacyList listItem : pharmacyLists) {
                    String name = listItem.getPharmacy_name().toLowerCase();
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getActivity();
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            filterBySpecialState(data.getStringExtra( "state"));
            // data.getStringExtra( "result");
            //some code
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
