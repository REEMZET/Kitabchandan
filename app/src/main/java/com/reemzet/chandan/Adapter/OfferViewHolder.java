package com.reemzet.chandan.Adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.reemzet.chandan.R;

public class OfferViewHolder extends RecyclerView.ViewHolder{

 public
 TextView offername,promocode;
 public ImageView deleteofferbtn;
    public OfferViewHolder(@NonNull View itemView) {
        super(itemView);
        offername=itemView.findViewById(R.id.offername);
        promocode=itemView.findViewById(R.id.promocode);
        deleteofferbtn=itemView.findViewById(R.id.deleteofferbtn);


    }
}
