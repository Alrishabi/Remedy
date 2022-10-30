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
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hiba.remedy.R;
import com.hiba.remedy.URLs.URL;
import com.hiba.remedy.activities.ProfileActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class LabsAdapter extends RecyclerView.Adapter<LabsAdapter.MyViewHolder> {

    private List<LabsList> labsLists;
    private Context context;
    private FragmentManager fm;
    private URL url;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, tv_special;
          public   RatingBar ratebare;
         public RelativeLayout relativeLayout;
        public ImageView imageV;


        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.hoste_name);
            tv_special = (TextView) view.findViewById(R.id.tv_special);
            ratebare = (RatingBar) view.findViewById(R.id.ratebare);
            imageV =(ImageView) view.findViewById(R.id.image_lap_view);
            relativeLayout =(RelativeLayout) view.findViewById(R.id.linearL);
        }
    }
    public LabsAdapter(List<LabsList> labsLists, Context context, FragmentManager fragmentManager) {
        this.labsLists = labsLists;
        this.context = context;
        this.fm=fragmentManager;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.labs_card, parent, false);

        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final LabsList itemList = labsLists.get(position);
        holder.name.setText( itemList.getLap_name());
        holder.ratebare.setRating(itemList.getLap_rate());
        holder.tv_special.setText(itemList.getLap_specialization());
        if(itemList.getLap_image()!=null && itemList.getLap_image().length()>0 ){
            Picasso.get().load(url.BASE_URL+itemList.getLap_image()).placeholder(R.drawable.helloandroid).into(holder.imageV);
        }else{
            Toast.makeText(context,"No image to view",Toast.LENGTH_LONG).show();

            Picasso.get().load(R.drawable.helloandroid).into(holder.imageV);
        }
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoProfile(v,itemList.getLap_id(),
                        itemList.getLap_name(),
                        url.BASE_URL+itemList.getLap_image(),
                        itemList.getLap_specialization(),
                        itemList.getThe_state(),
                        itemList.getLap_phone(),
                        itemList.getLap_address(),
                        itemList.getLap_location(),
                        itemList.getLap_rate());
            }
        });
    }
    @Override
    public int getItemCount() {
        return labsLists.size();
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
        bundle.putInt("ID_CATEGORY",3);
        bundle.putInt("ID_KEY",hospital_id);
        bundle.getString("Hospital_ID_KEY", "not hospital");
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

    public void setFilter(ArrayList<LabsList> newList)
    {
        labsLists = new ArrayList<>();
        labsLists.addAll(newList );
        notifyDataSetChanged();
    }
}