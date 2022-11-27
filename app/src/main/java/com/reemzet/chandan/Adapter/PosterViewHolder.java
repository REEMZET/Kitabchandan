package com.reemzet.chandan.Adapter;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.reemzet.chandan.R;

public class PosterViewHolder extends RecyclerView.ViewHolder {
   public ImageView posterimg,deleteposterbtn;
    public PosterViewHolder(@NonNull View itemView) {
        super(itemView);
        posterimg=itemView.findViewById(R.id.posterimg);
        deleteposterbtn=itemView.findViewById(R.id.deleteposterbtn);

    }
}
