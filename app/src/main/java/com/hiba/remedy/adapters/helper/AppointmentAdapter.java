package com.hiba.remedy.adapters.helper;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
import com.hiba.remedy.activities.ProfileActivity;
import com.hiba.remedy.fragments.Edit_Dialog_Fragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.MyViewHolder>{

    private List<AppointmentList> appointmentLists;
    private Context context;
    private FragmentManager fm;
    URL url;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, tv_special,tv_work,delete,edit;
        public RelativeLayout relativeLayout;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.hoste_name);
            tv_special = (TextView) view.findViewById(R.id.tv_special);
            tv_work = (TextView) view.findViewById(R.id.tv_work);
            delete = (TextView) view.findViewById(R.id.tv_delete);
            edit = (TextView) view.findViewById(R.id.tv_edit);
            relativeLayout =(RelativeLayout) view.findViewById(R.id.linearL);
        }
    }
    public AppointmentAdapter(List<AppointmentList> appointmentLists, Context context, FragmentManager fragmentManager) {
        this.appointmentLists = appointmentLists;
        this.context = context;
        this.fm=fragmentManager;
    }
    @Override
    public AppointmentAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.appointment_card, parent, false);

        return new AppointmentAdapter.MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(AppointmentAdapter.MyViewHolder holder, final int position) {
        final AppointmentList itemList = appointmentLists.get(position);
        holder.name.setText( itemList.getDoctor_name());
        holder.tv_special.setText(itemList.getDoctor_name());
        holder.tv_work.setText(itemList.getAppointment_date());
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoProfile(v);
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setIcon(R.drawable.delete)
                        .setTitle(R.string.attention_dialog_title)
                        .setMessage(context.getResources().getString(R.string.delete_appointment))
                        .setPositiveButton(context.getResources().getString(R.string.delete_hint),new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteItemFromDB(position);
                            }
                        }).setNegativeButton(R.string.cancel_button,null)
                        .show();
            }
        });

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//
                openDialogFragment(itemList.getAppointment_id(),
                                   itemList.getPatient_name(),
                                   itemList.getUser_phone(),
                                   itemList.getAppointment_date(),
                                   itemList.getDoctor_name(),
                                   itemList.getDoctor_phone(),
                    String.valueOf(itemList.getAppointment_price()),
                                   itemList.getAppointment_address()
                );


            }
        });
    }
    @Override
    public int getItemCount() {
        return appointmentLists.size();
    }

    public void gotoProfile(View view){
//        Intent intent = new Intent (view.getContext(), ProfileActivity.class);
//        view.getContext().startActivity(intent);
    }

    public void setFilter(ArrayList<AppointmentList> newList)
    {
        appointmentLists = new ArrayList<>();
        appointmentLists.addAll(newList );
        notifyDataSetChanged();
    }

    public void openDialogFragment(int appointment_id,
                                   String patient_name,
                                   String patient_phone,
                                   String appointment_date,
                                   String doctor_name,
                                   String doctor_phone,
                                   String appoint_price,
                                   String appointment_address
                                   ){
//        Bundle
        Bundle bundle=new Bundle();
        bundle.putInt("APPOINTMENT_ID_KEY",appointment_id);
        bundle.putString("PATIENT_KEY",patient_name);
        bundle.putString("PATIENT_PHONE_KEY",patient_phone);
        bundle.putString("APPOIN_DATE_KEY",appointment_date);
        bundle.putString("DOC_NAME_KEY",doctor_name);
        bundle.putString("DOC_PHONE_KEY",doctor_phone);
        bundle.putString("PRICE_KEY",appoint_price);
        bundle.putString("APPOINT_ADDRESS_KEY",appointment_address);


        Edit_Dialog_Fragment edit_dialog_fragment = new Edit_Dialog_Fragment();
        edit_dialog_fragment.setArguments(bundle);
        edit_dialog_fragment.show(fm,"eTag");
    }

    private void deleteItemFromDB(final int position){
        final AppointmentList listItem= appointmentLists.get(position);
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage( context.getResources().getString(R.string.deleting_progress));
        progressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, URL.BASE_URL + URL.DELETE_APPOINTMENT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("response",response);

                        try {
                            if(response.trim().equals("Record deleted successfully")){
                                Toast.makeText(context,R.string.delete_successfully, Toast.LENGTH_LONG).show();
                                appointmentLists.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position,appointmentLists.size());
                                notifyDataSetChanged();

                            }else {
                                Toast.makeText(context,response.trim().toString(), Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();

                            Log.d("JSONException",e.toString());
                            Toast.makeText(context,e.toString(),Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }
                       progressDialog.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,R.string.delete_filed, Toast.LENGTH_LONG).show();
               // Snackbar.make(mRoot, R.string.no_internet_connection,Snackbar.LENGTH_LONG).show();
              progressDialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();

                Log.d("patient_name",String.valueOf(listItem.getPatient_name()));
                parameters.put("appointment_id",String.valueOf(listItem.getAppointment_id()));
                parameters.put("doctor_id",String.valueOf(listItem.getDoctor_id()));
                parameters.put("appointment_date",String.valueOf(listItem.getAppointment_date()));

//                parameters.put("user_phone",user_phone);
//                parameters.put("appointment_price",appointment_price);
//                parameters.put("doctor_id",doctor_id);
//                parameters.put("doctor_name",doctor_name);
//                parameters.put("doctor_phone",doctor_phone);
//                parameters.put("appointment_address",appointment_address);
//                parameters.put("appointment_date",appointment_date);
//                parameters.put("user_id",user_id);

//                              {"appointment_id":"4",
//                                "patient_name":"",
//                                "user_phone":"",
//                                "appointment_price":"0",
//                                "doctor_id":"0",
//                                "doctor_name":"",
//                                "doctor_phone":"",
//                                "appointment_address":"",
//                                "appointment_date":"",
//                                "user_id":"0"}

                return parameters;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }
}
