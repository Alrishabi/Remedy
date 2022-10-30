package com.hiba.remedy.activities;

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
import com.hiba.remedy.adapters.helper.PatientAdapter;
import com.hiba.remedy.adapters.helper.PatientList;
import com.hiba.remedy.helpers.RemedyHelper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DisplayDoctorPatientActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private RecyclerView recyclerPharmacy;
    private ArrayList<PatientList> patientLists = new ArrayList<PatientList>();
   PatientAdapter mAdapter;
    RemedyHelper remedyHelper;
    Activity activity;
    SearchView.OnQueryTextListener b;
    SwipeRefreshLayout swipeRefreshLayout;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);
        initContext();
        initiateLayout();
    }

    public void initContext() {
        activity = this;
        this.remedyHelper=new RemedyHelper(activity);
        progressDialog = new ProgressDialog(activity);
    }
    public  void initiateLayout(){
//        setHasOptionsMenu(true);
        setTitle(R.string.patients_list);
        recyclerPharmacy = (RecyclerView)findViewById(R.id.recyclerView);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);

        final int    id        = getIntent().getExtras().getInt("ID_KEY");

        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setRecyclerPatient(String.valueOf(id));
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        mAdapter = new PatientAdapter(patientLists,getApplicationContext(),getSupportFragmentManager());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        recyclerPharmacy.setLayoutManager(mLayoutManager);
        recyclerPharmacy.setItemAnimator(new DefaultItemAnimator());
        recyclerPharmacy.setAdapter(mAdapter);

        progressDialog.setMessage(getResources().getString(R.string.loading_progress_dialog_message));

        setRecyclerPatient(String.valueOf(id));

    }

    public void setRecyclerPatient(final String doctor_id) {

        patientLists.clear();
//        swipeRefreshLayout.setRefreshing(true);
        progressDialog.show();

        StringRequest stringRequest =new StringRequest(Request.Method.POST,
                URL.BASE_URL+ URL.DISPLAY_PATIENT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            Log.d("Response", s);
                            JSONObject jsonObject= new JSONObject(s);
                            JSONArray array = jsonObject.getJSONArray("patient_table");
                            for(int i=0;i<array.length();i++){
                                JSONObject o = array.getJSONObject(i);
                                PatientList item= new PatientList(
                                        o.getInt("queue_id"),
                                        o.getString("patient_name"),
                                        o.getString("symptoms"),
                                        o.getString(  "tests"),
                                        o.getString("results"),
                                        o.getString("diagnosis"),
                                        o.getString("medicine"),
                                        o.getString("pharmacist_note"),
                                        o.getInt("doctor_id"),
                                        o.getString("doctor_name"),
                                        o.getInt("user_id"),
                                        o.getString("diagnosis_date"),
                                        o.getString("last_update"));
                                patientLists.add(item);
                                progressDialog.dismiss();

                            }
                            mAdapter =new PatientAdapter(patientLists,getApplicationContext(),getSupportFragmentManager());
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
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();

                parameters.put("doctor_id",doctor_id);

                return parameters;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
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
                ArrayList<PatientList> newList = new ArrayList<>();
                for (PatientList listItem : patientLists) {
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
