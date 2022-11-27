package com.reemzet.chandan.Adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.reemzet.chandan.Models.CartModel;
import com.reemzet.chandan.R;


import java.util.List;

public class MoreOrderDetailsAdapter extends RecyclerView.Adapter<MoreOrderDetailsAdapter.MoreOrderDetailsViewholder> {

    Context context;
    List<CartModel> cartModels;

    public MoreOrderDetailsAdapter(Context context, List<CartModel> cartModels) {
        this.context = context;
        this.cartModels = cartModels;
    }



    @NonNull
    @Override
    public MoreOrderDetailsViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.myorderdetailslayout, parent, false);
        return new MoreOrderDetailsViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoreOrderDetailsViewholder holder, int position) {
        CartModel model=cartModels.get(position);
        holder.itemname.setText(model.getItemname());
        holder.itemmrp.setPaintFlags(holder.itemmrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.itemmrp.setText("mrp:" + String.valueOf(Integer.parseInt(model.getItemmrp()) * Integer.parseInt(model.getItemqty())));
        holder.itemqty.setText(model.getItemqty());
        holder.itemprice.setText("\u20B9" + String.valueOf(Integer.parseInt(model.getItemprice()) * Integer.parseInt(model.getItemqty())));

        Glide.with(context)
                .load(model.getItemimg())
                .centerCrop()
                .placeholder(R.drawable.books)
                .into(holder.itemimg);
    }

    @Override
    public int getItemCount() {
        return cartModels.size();
    }

    public class MoreOrderDetailsViewholder extends RecyclerView.ViewHolder {
        TextView itemname,itemqty,itemmrp,itemprice;
        ImageView itemimg;
        public MoreOrderDetailsViewholder(@NonNull View itemView) {
            super(itemView);
            itemname=itemView.findViewById(R.id.cartitemname);
            itemqty=itemView.findViewById(R.id.itemqty);
            itemmrp=itemView.findViewById(R.id.cartitemmrp);
            itemprice=itemView.findViewById(R.id.cartitemprice);
            itemimg=itemView.findViewById(R.id.cartitemimg);
        }
    }
}
