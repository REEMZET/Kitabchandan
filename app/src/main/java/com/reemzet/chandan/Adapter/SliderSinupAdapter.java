package com.reemzet.chandan.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.reemzet.chandan.Models.ViewpagerModel;
import com.reemzet.chandan.R;

import java.util.ArrayList;

public class SliderSinupAdapter extends RecyclerView.Adapter<SliderSinupAdapter.SliderViewHolder> {
    Context context;
    ArrayList<ViewpagerModel> viewpagerModelArrayList;
    ViewPager2 viewPager2;

    public SliderSinupAdapter(Context context, ArrayList<ViewpagerModel> viewpagerModelArrayList, ViewPager2 viewPager2) {
        this.context = context;
        this.viewpagerModelArrayList = viewpagerModelArrayList;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SliderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.signupsliderlayout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
        ViewpagerModel model=viewpagerModelArrayList.get(position);
        holder.lottie.setAnimationFromUrl(model.getPosterurl());
        if (position==viewpagerModelArrayList.size() - 2){
            viewPager2.post(runnable);
        }
    }

    @Override
    public int getItemCount() {
        return viewpagerModelArrayList.size();
    }

    public class SliderViewHolder extends RecyclerView.ViewHolder{
        LottieAnimationView lottie;
        public SliderViewHolder(@NonNull View itemView) {
            super(itemView);
            lottie=itemView.findViewById(R.id.lottie);
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
