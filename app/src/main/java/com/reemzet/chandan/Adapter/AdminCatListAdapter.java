package com.reemzet.chandan.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.FirebaseDatabase;
import com.reemzet.chandan.R;

import java.util.ArrayList;

public class AdminCatListAdapter extends RecyclerView.Adapter<AdminCatListAdapter.Viewholder> {
    Context context;
    ArrayList<String> arrayList;
    NavController navController;
    FirebaseDatabase database;

    public AdminCatListAdapter(Context context, ArrayList<String> arrayList, NavController navController) {
        this.context = context;
        this.arrayList = arrayList;
        this.navController = navController;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View listitemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.admincatlistlayout, parent, false);
        return new AdminCatListAdapter.Viewholder(listitemview);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        database= FirebaseDatabase.getInstance();
        holder.tvcatname.setText(arrayList.get(position));
        holder.listlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putString("category",holder.tvcatname.getText().toString());
                navController.navigate(R.id.action_adminCatList_to_adminCatPage,bundle);

            }
        });
        holder.deletecat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(context)
                        .setTitle("Are you sure?")
                        .setMessage("Do you want to delete this cateogory?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                database.getReference("App/Items").child(arrayList.get(holder.getAbsoluteAdapterPosition())).removeValue();
                                Toast.makeText(context, "Cateogory deleted", Toast.LENGTH_SHORT).show();
                                arrayList.remove(holder.getAbsoluteAdapterPosition());
                                notifyItemRemoved(holder.getAbsoluteAdapterPosition());
                            }
                        }).setNegativeButton("No", null).show();




            }
        });


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder{
        TextView tvcatname;
        LinearLayout listlayout;
        ImageView deletecat;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            listlayout=itemView.findViewById(R.id.listlayout);
            tvcatname=itemView.findViewById(R.id.tvcatname);
            deletecat=itemView.findViewById(R.id.deletecat);
        }
    }
}
