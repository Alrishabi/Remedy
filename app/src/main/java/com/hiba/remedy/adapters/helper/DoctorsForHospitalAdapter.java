package com.hiba.remedy.adapters.helper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hiba.remedy.R;
import com.hiba.remedy.URLs.URL;
import com.hiba.remedy.activities.ProfileActivity;
import com.hiba.remedy.helpers.RemedyHelper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DoctorsForHospitalAdapter extends RecyclerView.Adapter<DoctorsForHospitalAdapter.MyViewHolder>{

    private List<DoctorsList> doctorsLists;
    private Context context;
    private FragmentManager fm;
    RemedyHelper remedyHelper;
    URL url;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, tv_special,tv_work;
        public RelativeLayout relativeLayout;
        ImageView image_doc_view;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.hoste_name);
            tv_special = (TextView) view.findViewById(R.id.tv_special);
            tv_work = (TextView) view.findViewById(R.id.tv_work);
            relativeLayout =(RelativeLayout) view.findViewById(R.id.linearL);
            image_doc_view = (ImageView) view.findViewById(R.id.image_doc_view);

        }
    }
    public DoctorsForHospitalAdapter(List<DoctorsList> doctorsLists, Context context, FragmentManager fragmentManager,RemedyHelper remedyHelper) {
        this.doctorsLists = doctorsLists;
        this.context = context;
        this.fm=fragmentManager;
        this.remedyHelper=remedyHelper;
    }
    @Override
    public DoctorsForHospitalAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.doctor_card, parent, false);

        return new DoctorsForHospitalAdapter.MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(DoctorsForHospitalAdapter.MyViewHolder holder, int position) {
        final DoctorsList itemList = doctorsLists.get(position);
        holder.name.setText( itemList.getDoctor_name());
        holder.tv_special.setText(itemList.getDoctor_specialization());
        holder.tv_work.setText(itemList.getDoctor_Workplace());
        if(itemList.getDoctor_image()!=null && itemList.getDoctor_image().length()>0 ){
            Picasso.get().load(url.BASE_URL+itemList.getDoctor_image()).placeholder(R.drawable.helloandroid).into(holder.image_doc_view);
        }else{
            Toast.makeText(context,"No image to view",Toast.LENGTH_LONG).show();

            Picasso.get().load(R.drawable.helloandroid).into(holder.image_doc_view);
        }
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoProfile(v,itemList.getDoctor_id(),
                        itemList.getDoctor_name(),
                        url.BASE_URL+itemList.getDoctor_image(),
                        itemList.getDoctor_specialization(),
                        itemList.getThe_state(),
                        itemList.getAppointment_price(),
                        itemList.getDoctor_phone(),
                        itemList.getDoctor_Workplace()
                      );
            }
        });
    }
    @Override
    public int getItemCount() {
        return doctorsLists.size();
    }

    public void gotoProfile(View view,int hospital_id,
                            String hospital_name,
                            String hospital_image_url,
                            String hospital_specialization,
                            String the_state,
                            Float price,
                            String hospital_phone,
                            String hospital_address){


        //Bundle
        Bundle bundle=new Bundle();
        bundle.putInt("ID_CATEGORY",2);
        bundle.putInt("ID_KEY",hospital_id);
        bundle.putString("NAME_KEY",hospital_name);
        bundle.putString("IMAGE_URL_KEY",hospital_image_url);
        bundle.putString("SPECIALIZATION_KEY",hospital_specialization);
        bundle.putString("STATE_KEY",the_state);
        remedyHelper.log("PRICE_IN_DOC_ADAPTER", String.valueOf(price));
        bundle.putFloat("PRICE_KEY",price);
        bundle.putString("PHONE_KEY",hospital_phone);
        bundle.putString("ADDRESS_KEY",hospital_address);
        bundle.putString("LOCATION_KEY","");
        bundle.putFloat("RATE_KEY",0);

        Intent intent = new Intent (view.getContext(), ProfileActivity.class);
        intent.putExtras(bundle);

        view.getContext().startActivity(intent);
    }

    public void setFilter(ArrayList<DoctorsList> newList)
    {
        doctorsLists = new ArrayList<>();
        doctorsLists.addAll(newList );
        notifyDataSetChanged();
    }
}
