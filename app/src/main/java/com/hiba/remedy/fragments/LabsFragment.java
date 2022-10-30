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
import com.hiba.remedy.activities.FilterPharmacistAndLabsActivity;
import com.hiba.remedy.activities.LoginActivity;
import com.hiba.remedy.adapters.helper.LabsAdapter;
import com.hiba.remedy.adapters.helper.LabsList;
import com.hiba.remedy.adapters.helper.PharmacyList;
import com.hiba.remedy.helpers.RemedyHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

@SuppressLint("ValidFragment")
public class LabsFragment extends Fragment implements SearchView.OnQueryTextListener {
    private RecyclerView recyclerview;
    private ArrayList<LabsList> labsLists = new ArrayList<LabsList>();
    LabsAdapter mAdapter;
    SwipeRefreshLayout swipeRefreshLayout;
    ProgressDialog progressDialog;
    Activity activity;
    RemedyHelper remedyHelper;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View rootView =inflater.inflate(R.layout.fragment_laps, container, false);

        initiateLayout(rootView);
        setRecyclerviewLaps();
    return rootView;
    }

    @SuppressLint("ValidFragment")
    public LabsFragment(Activity activity) {
        this.activity = activity;
        remedyHelper=new RemedyHelper(activity);
        progressDialog = new ProgressDialog(activity);
    }
    public  void initiateLayout(View view){
        setHasOptionsMenu(true);
     //  getActivity().setTitle(R.string.title_Laps);

        mAdapter = new LabsAdapter(labsLists,getContext(),getFragmentManager());

        recyclerview       = (RecyclerView) view.findViewById(R.id.recyclerView);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.laps_swap);

        // mAdapter = new HospitalAdapter(hosplist);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerview.setLayoutManager(mLayoutManager);
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.setAdapter(mAdapter);
        progressDialog.setMessage(getResources().getString(R.string.loading_progress_dialog_message));
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.laps_swap);

        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setRecyclerviewLaps();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    public void setRecyclerviewLaps() {
        labsLists.clear();
        StringRequest stringRequest =new StringRequest(Request.Method.GET,
                URL.BASE_URL+URL.DISPLAY_LABS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            Log.d("Response", s);
                            JSONObject jsonObject= new JSONObject(s);
                            JSONArray array = jsonObject.getJSONArray("labs_table");
                            for(int i=0;i<array.length();i++){
                                JSONObject o = array.getJSONObject(i);
                                LabsList item= new LabsList(
                                        o.getInt("lab_id"),
                                        o.getString("lab_name"),
                                        o.getString(  "lab_image"),
                                        o.getString("lab_specialization"),
                                        o.getString("the_state"),
                                        o.getString("lab_phone"),
                                        o.getLong("lab_rate"),
                                        o.getString("lab_address"),
                                        o.getString("lab_location"));

//                                int lap_id;
//                                String lap_name;
//                                String lap_image;
//                                String lap_specialization;
//                                int lap_phone;
//                                float lap_rate;
//                                String lap_address;
//                                String lap_location;

                                labsLists.add(item);
                            }
                            mAdapter =new LabsAdapter(labsLists,getContext(),getFragmentManager());
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
                        Toast.makeText(getActivity(),R.string.connection_failed,Toast.LENGTH_LONG).show();
                    }
                });
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
                ArrayList<LabsList> newList = new ArrayList<>();
                for (LabsList listItem : labsLists) {
                    String name = listItem.getLap_name().toLowerCase();
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
            startActivityForResult(new Intent(getActivity(), FilterPharmacistAndLabsActivity.class),1);
        }
        return super.onOptionsItemSelected(item);
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

    public void filterBySpecialState( String state) {
        if(state.equals("-محليه-")){
            Toast.makeText(getContext(),getResources().getString(R.string.no_thing_select_it)+" "+getResources().getString(R.string.labs), Toast.LENGTH_LONG).show();
        }else if( !state.equals("-محليه-")){
            try {
                if (state == null) {
                    Toast.makeText(getContext(),R.string.not_item_found, Toast.LENGTH_LONG).show();
                } else {
//              newText = newText.toLowerCase();
                    ArrayList<LabsList> newList = new ArrayList<>();
                    for (LabsList listItem : labsLists) {
                        String theState = listItem.getThe_state().toLowerCase();
                        if (theState.contains(state)) {
                            newList.add(listItem);
                        }
                    }
                    mAdapter.setFilter(newList);
                }
                Toast.makeText(activity,getResources().getString(R.string.filter_it)+" "+state, Toast.LENGTH_LONG).show();
            }catch (Exception e){
                Log.d( "Exception: ",e.toString());
                Log.d( "nothing_to_search_it: ","nothing");
            }
        }

    }
}
