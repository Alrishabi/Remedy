package com.hiba.remedy.adapters.helper;

import android.content.Context;
import android.content.Intent;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PharmacyAdapter extends RecyclerView.Adapter<PharmacyAdapter.MyViewHolder>{

    private List<PharmacyList> pharmacyLists;
    private Context context;
    private FragmentManager fm;
    URL url;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView name, tv_special;
        public RatingBar ratebare;
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
    public PharmacyAdapter(List<PharmacyList> pharmacyLists, Context context, FragmentManager fragmentManager) {
        this.pharmacyLists = pharmacyLists;
        this.context = context;
        this.fm=fragmentManager;
    }
    @Override
    public PharmacyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pharmacy_list_item, parent, false);

        return new PharmacyAdapter.MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(PharmacyAdapter.MyViewHolder holder, int position) {
        final PharmacyList itemList = pharmacyLists.get(position);
        holder.name.setText( itemList.getPharmacy_name());
        holder.ratebare.setRating(itemList.getPharmacy_rate());
        holder.tv_special.setText(itemList.getThe_state());
        if(itemList.getPharmacy_image()!=null && itemList.getPharmacy_image().length()>0 ){
            Picasso.get().load(URL.BASE_URL +itemList.getPharmacy_image()).placeholder(R.drawable.helloandroid).into(holder.imageV);
        }else{
            Toast.makeText(context,"No image to view",Toast.LENGTH_LONG).show();
            Picasso.get().load(R.drawable.helloandroid).into(holder.imageV);
        }
//      new GetImageFromURL(holder.imageView).execute(itemList.getHospital_image());
        Log.d("imageURl",itemList.getPharmacy_image());
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoProfile(v,itemList.getPharmacy_id(),
                        itemList.getPharmacy_name(),
                        URL.BASE_URL +itemList.getPharmacy_image(),
                        itemList.getThe_state(),
                        itemList.getPharmacy_phone(),
                        itemList.getPharmacy_address(),
                        itemList.getPharmacy_location(),
                        itemList.getPharmacy_rate());
            }
        });

    }
    @Override
    public int getItemCount() {
        return pharmacyLists.size();
    }

    public void gotoProfile(View view,int hospital_id,
                            String hospital_name,
                            String hospital_image_url,
                            String the_state,
                            String hospital_phone,
                            String hospital_address,
                            String hospital_location,
                            Float hospital_rate){
        //Bundle
        Bundle bundle=new Bundle();
        bundle.putInt("ID_CATEGORY",4);
        bundle.putInt("ID_KEY",hospital_id);
        bundle.putString("NAME_KEY",hospital_name);
        bundle.putString("IMAGE_URL_KEY",hospital_image_url);
        bundle.putString("SPECIALIZATION_KEY","صيدلية" );
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

    public void setFilter(ArrayList<PharmacyList> newList)
    {
        pharmacyLists = new ArrayList<>();
        pharmacyLists.addAll(newList );
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
