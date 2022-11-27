package com.reemzet.chandan.Adapter;

import android.media.Image;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.reemzet.chandan.R;

public class CartViewHolder extends RecyclerView.ViewHolder {
    public TextView itemname,itemqty,itemmrp,itemprice,btnincrement,btndecrement;
    public ImageView itemimg,btndelete;
    public CartViewHolder(@NonNull View itemView) {
        super(itemView);
        itemname=itemView.findViewById(R.id.cartitemname);
        itemqty=itemView.findViewById(R.id.itemqty);
        itemmrp=itemView.findViewById(R.id.cartitemmrp);
        itemprice=itemView.findViewById(R.id.cartitemprice);
        itemimg=itemView.findViewById(R.id.cartitemimg);
        btnincrement=itemView.findViewById(R.id.itemincrementbtn);
        btndecrement=itemView.findViewById(R.id.itemdcrementbtn);
        btndelete=itemView.findViewById(R.id.deletebtn);

    }
}
