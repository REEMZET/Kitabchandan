package com.reemzet.chandan.Adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.reemzet.chandan.R;

import org.checkerframework.checker.index.qual.PolyUpperBound;

public class MyorderViewholder extends RecyclerView.ViewHolder {
 public TextView orderdate,paymentoption,ordertime,ordertitle,orderqty,ordertotalprice,orderid,orderstatus,deliveryaddress,username,viewmore;
    public ImageView locationimg;
    public MyorderViewholder(@NonNull View itemView) {
        super(itemView);
        orderdate=itemView.findViewById(R.id.orderdate);
        paymentoption=itemView.findViewById(R.id.paymentoption);
        ordertime=itemView.findViewById(R.id.ordertime);

        ordertotalprice=itemView.findViewById(R.id.ordertotalprice);
        orderid=itemView.findViewById(R.id.orderid);
        orderstatus=itemView.findViewById(R.id.orderstatus);
        viewmore=itemView.findViewById(R.id.viewmore);
        username=itemView.findViewById(R.id.username);


    }
}
