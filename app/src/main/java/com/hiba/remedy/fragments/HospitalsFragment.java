package com.hiba.remedy.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
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
import com.hiba.remedy.activities.LoginActivity;
import com.hiba.remedy.adapters.helper.HospitalAdapter;
import com.hiba.remedy.adapters.helper.HospitalList;
import com.hiba.remedy.helpers.RemedyHelper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

@SuppressLint("ValidFragment")
public class HospitalsFragment extends Fragment implements SearchView.OnQueryTextListener {
    private RecyclerView recyclerviewHospit;
    private ArrayList<HospitalList> hosplist = new ArrayList<HospitalList>();
    HospitalAdapter mAdapter;
    RemedyHelper remedyHelper;
    Activity activity;
    SwipeRefreshLayout swipeRefreshLayout;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View rootView= inflater.inflate(R.layout.fragment_hospitals, container, false);

        initiateLayout(rootView);

        setRecyclerviewHospital();
       // activity.setTitle(R.string.title_hospitals);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setRecyclerviewHospital();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        /**
         * Showing Swipe Refresh animation on activity create
         * As animation won't start on onCreate, post runnable is used
         */
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public HospitalsFragment(Activity activity) {
        this.activity = activity;
        this.remedyHelper=new RemedyHelper(activity);
        progressDialog = new ProgressDialog(activity);
    }

    public  void initiateLayout(View view){
        setHasOptionsMenu(true);

        recyclerviewHospit = (RecyclerView) view.findViewById(R.id.recyclerView);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.hospital_swapRefresh);


        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        mAdapter = new HospitalAdapter(hosplist,getContext(),getFragmentManager(),remedyHelper);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerviewHospit.setLayoutManager(mLayoutManager);
        recyclerviewHospit.setItemAnimator(new DefaultItemAnimator());
        recyclerviewHospit.setAdapter(mAdapter);

        progressDialog.setMessage(getResources().getString(R.string.loading_progress_dialog_message));

//        swipeRefreshLayout.setOnRefreshListener(this);
//        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
//                android.R.color.holo_green_dark,
//                android.R.color.holo_orange_dark,
//                android.R.color.holo_blue_dark);

    }

    public void setRecyclerviewHospital() {

    hosplist.clear();
//        swipeRefreshLayout.setRefreshing(true);
        progressDialog.show();

        StringRequest stringRequest =new StringRequest(Request.Method.GET,
               URL.BASE_URL+URL.DISPLAY_HOSPITALS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            Log.d("Response", s);
                            JSONObject jsonObject= new JSONObject(s);
                            JSONArray array = jsonObject.getJSONArray("hospitals_table");
                            for(int i=0;i<array.length();i++){
                                JSONObject o = array.getJSONObject(i);
                                HospitalList item= new HospitalList(
                                        o.getInt("hospital_id"),
                                        o.getString("hospital_name"),
                                        o.getString(  "hospital_image"),
                                        o.getString("hospital_specialization"),
                                        o.getString("the_state"),
                                        o.getString("hospital_phone"),
                                        o.getString("hospital_address"),
                                        o.getString("hospital_location"),
                                        o.getLong("hospital_rate"),
                                        o.getInt("doctor_id"));

//                                {"hospital_id":"1",
//                                        "hospital_name":"TEST",
//                                        "hospital_image":"TEST",
//                                        "hospital_specialization":"TEST",
//                                        "hospital_phone":"3899792",
//                                        "hospital_address":"TEST",
//                                        "hospital_location":" VKCEVFK V",
//                                        "hospital_rate":"5",
//                                        "doctor_id":"1"},
                                hosplist.add(item);
                                progressDialog.dismiss();

                            }
                            mAdapter =new HospitalAdapter(hosplist,getContext(),getFragmentManager(),remedyHelper);
                            recyclerviewHospit.setAdapter(mAdapter);
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
                        Toast.makeText(getContext(),R.string.connection_failed,Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                });
        RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
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
            startActivityForResult(new Intent(getActivity(), FilterActivity.class),1);
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
                ArrayList<HospitalList> newList = new ArrayList<>();
                for (HospitalList listItem : hosplist) {
                    String name = listItem.getHospital_name().toLowerCase();
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

    public void filterBySpecialState(String special, String state) {
        if(!special.equals("-تخصص-") && state.equals("-محليه-")){
            try {
                if (special == null) {
                    Toast.makeText(getContext(),R.string.not_item_found, Toast.LENGTH_LONG).show();
                } else {
//              newText = newText.toLowerCase();
                    ArrayList<HospitalList> newList = new ArrayList<>();
                    for (HospitalList listItem : hosplist) {
                        String name = listItem.getHospital_specialization().toLowerCase();
                        if (name.contains(special)) {
                            newList.add(listItem);
                        }
                    }
                    mAdapter.setFilter(newList);
                }
                Toast.makeText(activity,R.string.filter_it+" "+special, Toast.LENGTH_LONG).show();
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
                    ArrayList<HospitalList> newList = new ArrayList<>();
                    for (HospitalList listItem : hosplist) {
                        String theState = listItem.getThe_state().toLowerCase();
                        if (theState.contains(state)) {
                            newList.add(listItem);
                        }
                    }
                    mAdapter.setFilter(newList);
                }
                Toast.makeText(activity,R.string.filter_it+" "+state, Toast.LENGTH_LONG).show();
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
                    ArrayList<HospitalList> newList = new ArrayList<>();
                    for (HospitalList listItem : hosplist) {
                        String theState = listItem.getThe_state().toLowerCase();
                        String spical = listItem.getHospital_specialization().toLowerCase();
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

    public void logOut() {

        remedyHelper.setValueToKey("is_login","0");
        remedyHelper.setValueToKey("user_id","");
        remedyHelper.setValueToKey("user_name","");
        remedyHelper.setValueToKey("user_phone","");
        remedyHelper.setValueToKey("security_qu_answer","");
        remedyHelper.setValueToKey("user_category","");

        startActivity(new Intent(getActivity(), LoginActivity.class));

        activity.finish();
    }
}
