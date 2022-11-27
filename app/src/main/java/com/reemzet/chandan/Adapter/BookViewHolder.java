package com.reemzet.chandan.Adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.reemzet.chandan.R;

public class BookViewHolder extends RecyclerView.ViewHolder {
        public TextView booktile,bookmrp,bookprice,bookstatus;
        public ImageView bookimg,removeitem;
        public ConstraintLayout itemconstraint;
    public BookViewHolder(@NonNull View itemView) {
        super(itemView);
            booktile=itemView.findViewById(R.id.booktitle);
            bookimg=itemView.findViewById(R.id.bookimg);
            bookprice=itemView.findViewById(R.id.bookprice);
            bookstatus=itemView.findViewById(R.id.bookstatus);
            bookmrp=itemView.findViewById(R.id.bookmrp);
            itemconstraint=itemView.findViewById(R.id.itemconstraint);
                }
}
