package com.hiba.remedy.adapters.helper;



import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.hiba.remedy.helpers.RemedyHelper;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class HospitalAdapter extends RecyclerView.Adapter<HospitalAdapter.MyViewHolder> {

    private List<HospitalList> hospitalLists;
    private Context context;
    private FragmentManager fm;
    RemedyHelper remedyHelper;
    URL url;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView name, tv_special;
          public   RatingBar ratebare;
         public RelativeLayout relativeLayout;
        public ImageView imageV;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.hoste_name);
            tv_special = (TextView) view.findViewById(R.id.tv_special);
            ratebare = (RatingBar) view.findViewById(R.id.ratebare);
            imageV = (ImageView) view.findViewById(R.id.image_host_view);
            relativeLayout =(RelativeLayout) view.findViewById(R.id.linearL);
        }
    }
    public HospitalAdapter(List<HospitalList> hospitalLists, Context context, FragmentManager fragmentManager,
                           RemedyHelper remedyHelper) {
        this.hospitalLists = hospitalLists;
        this.context = context;
        this.fm=fragmentManager;
        this.remedyHelper=remedyHelper;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hospi_list_item, parent, false);

        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final HospitalList itemList = hospitalLists.get(position);
        holder.name.setText( itemList.getHospital_name());
        holder.ratebare.setRating(itemList.getHospital_rate());
        holder.tv_special.setText(itemList.getHospital_specialization());
        if(itemList.getHospital_image_url()!=null && itemList.getHospital_image_url().length()>0 ){
            Picasso.get().load(url.BASE_URL+itemList.getHospital_image_url()).placeholder(R.drawable.helloandroid).into(holder.imageV);
        }else{
            Toast.makeText(context,"No image to view",Toast.LENGTH_LONG).show();

            Picasso.get().load(R.drawable.helloandroid).into(holder.imageV);
        }
//      new GetImageFromURL(holder.imageView).execute(itemList.getHospital_image());
        Log.d("imageURl",itemList.getHospital_image_url());
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                gotoProfile(v,itemList.getHospital_id(),
                        itemList.getHospital_name(),
                        url.BASE_URL+itemList.getHospital_image_url(),
                        itemList.getHospital_specialization(),
                        itemList.getThe_state(),
                        itemList.getHospital_phone(),
                        itemList.getHospital_address(),
                        itemList.getHospital_location(),
                        itemList.getHospital_rate());
            }
        });

    }
    @Override
    public int getItemCount() {
        return hospitalLists.size();
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
        remedyHelper.setValueToKey("Hospital_ID_KEY",String.valueOf(hospital_id));
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

    public void setFilter(ArrayList<HospitalList> newList)
    {
        hospitalLists = new ArrayList<>();
        hospitalLists.addAll(newList );
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