package com.hiba.remedy.adapters.helper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hiba.remedy.R;
import com.hiba.remedy.URLs.URL;
import com.hiba.remedy.activities.PatientProfileActivity;
import com.hiba.remedy.activities.ProfileActivity;

import java.util.ArrayList;
import java.util.List;

public class MyMeetAdapter extends RecyclerView.Adapter<MyMeetAdapter.MyViewHolder>{

    private List<PatientList> patientLists;
    private Context context;
    private FragmentManager fm;
    URL url;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        //    {"queue_id":"0",
//    "patient_name":"",
//    "symptoms":"",
//    "tests":"",
//    "results":"",
//    "diagnosis":"",
//    "medicine":"",
//    "pharmacist_note":"",
//    "doctor_id":"1",
//    "user_id":"1"},
        private TextView tv_patient_name,
                tv_symptoms;
        private RelativeLayout relative_card;

        private MyViewHolder(View view) {
            super(view);
            tv_patient_name = (TextView) view.findViewById(R.id.tv_patient_name);
            tv_symptoms = (TextView) view.findViewById(R.id.tv_symptoms);
            relative_card = (RelativeLayout) view.findViewById(R.id.relative_card);

        }
    }
    public MyMeetAdapter(List<PatientList> patientLists, Context context, FragmentManager fragmentManager) {
        this.patientLists = patientLists;
        this.context = context;
        this.fm=fragmentManager;
    }
    @Override
    public MyMeetAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_meet_list_item, parent, false);

        return new MyMeetAdapter.MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final PatientList itemList = patientLists.get(position);
        holder.tv_patient_name.setText( itemList.getDoctor_name());
        holder.tv_symptoms.setText(itemList.getDiagnosis_date());
        holder.relative_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("QUEUE_ID",String.valueOf(itemList.getQueue_id()) );
                Log.d("DOCTOR_ID",String.valueOf(itemList.getDoctor_id()) );
                gotoToPatientProfile(view,
                        itemList.getPatient_name(),
                        itemList.getQueue_id(),
                        itemList.getSymptoms(),
                        itemList.getTests(),
                        itemList.getResults(),
                        itemList.getDiagnosis(),
                        itemList.getMedicine(),
                        itemList.getPharmacist_note(),
                        itemList.getDoctor_id()
                        );
            }
        });
//      new GetImageFromURL(holder.imageView).execute(itemList.getHospital_image());
//        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                gotoProfile(v,itemList.getHospital_id(),
//                        itemList.g(),
//                        url.BASE_URL+itemList.getHospital_image_url(),
//                        itemList.getHospital_specialization(),
//                        itemList.getThe_state(),
//                        itemList.getHospital_phone(),
//                        itemList.getHospital_address(),
//                        itemList.getHospital_location(),
//                        itemList.getHospital_rate());
//            }
//        });
    }
    @Override
    public int getItemCount() {
        return patientLists.size();
    }

    private void gotoToPatientProfile(View view,
                                      String patient_name,
                                      int queue_id,
                                      String symptoms,
                                      String tests,
                                      String results,
                                      String diagnosis,
                                      String medicine,
                                      String pharmacist,
                                      int doctor_id
                                      ){
        //Bundle
        Bundle bundle=new Bundle();
        bundle.putString("PATIENT_KEY",patient_name);
        bundle.putInt("QUEUE_ID_KEY",queue_id);
        bundle.putString("SYMPTOMS_KEY",symptoms);
        bundle.putString("TESTS_KEY",tests);
        bundle.putString("RESULTS_KEY",results);
        bundle.putString("DIAGNOSIS_KEY",diagnosis);
        bundle.putString("MEDICINE_KEY",medicine);
        bundle.putString("PHARMACIST_KEY",pharmacist);
        bundle.putInt("DOCTOR_ID_KEY",doctor_id);

        Intent intent = new Intent (view.getContext(),PatientProfileActivity.class);
        intent.putExtras(bundle);
        view.getContext().startActivity(intent);
    }

    public void gotoProfile(View view,int hospital_id,
                            String hospital_name,
                            String hospital_image_url,
                            String hospital_specialization,
                            String the_state,
                            String hospital_phone,
                            String hospital_address,
                            String hospital_location,
                            Float hospital_rate){
        //Bundle
        Bundle bundle=new Bundle();
        bundle.putInt("ID_CATEGORY",1);
        bundle.putInt("ID_KEY",hospital_id);
        bundle.putString("NAME_KEY",hospital_name);
        bundle.putString("IMAGE_URL_KEY",hospital_image_url);
        bundle.putString("SPECIALIZATION_KEY",hospital_specialization);
        bundle.putString("STATE_KEY",the_state);
        bundle.putFloat("PRICE_KEY",0);
        bundle.putString("PHONE_KEY",hospital_phone);
        bundle.putString("ADDRESS_KEY",hospital_address);
        bundle.putString("LOCATION_KEY",hospital_location);
        bundle.putFloat("RATE_KEY",hospital_rate);

        Intent intent = new Intent (view.getContext(), ProfileActivity.class);
        intent.putExtras(bundle);

        view.getContext().startActivity(intent);
    }

    public void setFilter(ArrayList<PatientList> newList){
        patientLists = new ArrayList<>();
        patientLists.addAll(newList );
        notifyDataSetChanged();
    }

//    public class GetImageFromURL extends AsyncTask<String,Void, Bitmap>
//    {
//        ImageView imageView;
//        public GetImageFromURL(ImageView imgv) {
//            this.imageView=imgv;
//        }
//
//        @Override
//        protected Bitmap doInBackground(String... url) {
//            String urlDisplay =url[0];
//            bitmap=null;
//
//            try {
//                InputStream ist=new java.net.URL(urlDisplay).openStream();
//                bitmap= BitmapFactory.decodeStream(ist);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return bitmap;
//        }
//
//        @Override
//        protected void onPostExecute(Bitmap bitmap) {
//            super.onPostExecute(bitmap);
//            imageView.setImageBitmap(bitmap);
//        }
//    }
}
