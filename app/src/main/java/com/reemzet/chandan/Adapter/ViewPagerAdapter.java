package com.reemzet.chandan.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.reemzet.chandan.R;
import com.reemzet.chandan.Models.ViewpagerModel;

import java.util.ArrayList;


public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.Myviewholder> {

    Context context;
    ArrayList<ViewpagerModel> viewpagerModelArrayList;
    ViewPager2 viewPager2;

    public ViewPagerAdapter(Context context, ArrayList<ViewpagerModel> viewpagerModelArrayList, ViewPager2 viewPager2) {
        this.context = context;
        this.viewpagerModelArrayList = viewpagerModelArrayList;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public Myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Myviewholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_layout_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull Myviewholder holder, int position) {
        ViewpagerModel model=viewpagerModelArrayList.get(position);
        Glide.with(context).load(model.getPosterurl())
                .into(holder.imageView);
        if (position==viewpagerModelArrayList.size() - 2){
            viewPager2.post(runnable);
        }
    }

    @Override
    public int getItemCount() {
        return viewpagerModelArrayList.size();
    }


    public  class Myviewholder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public Myviewholder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_auto_image_slider);


        }
    }
        public Runnable runnable=new Runnable() {
            @Override
            public void run() {
                viewpagerModelArrayList.addAll(viewpagerModelArrayList);
                notifyDataSetChanged();
            }
        };

    }



