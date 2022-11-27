package com.reemzet.chandan.Adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.reemzet.chandan.R;

public class AdminOrderViewHOLDER extends RecyclerView.ViewHolder {
    public TextView paymentoption,ordertime,ordertotalprice,orderid,orderstatus;

    public AdminOrderViewHOLDER(@NonNull View itemView) {
        super(itemView);

        paymentoption=itemView.findViewById(R.id.paymentoption);
        ordertime=itemView.findViewById(R.id.ordertime);
        ordertotalprice=itemView.findViewById(R.id.ordertotalprice);
        orderid=itemView.findViewById(R.id.orderid);
        orderstatus=itemView.findViewById(R.id.orderstatus);

    }
}
