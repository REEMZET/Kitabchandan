package com.reemzet.chandan.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.reemzet.chandan.R;

import java.util.ArrayList;

public class ChatlistAdapter extends RecyclerView.Adapter<ChatlistAdapter.viewholder> {
      Context context;
      ArrayList<String>list;
      NavController navController;
    FirebaseDatabase database;
    DatabaseReference useref;
    String username;

    public ChatlistAdapter(Context context, ArrayList<String> list, NavController navController) {
        this.context = context;
        this.list = list;
        this.navController = navController;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View listitemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatlistlayout, parent, false);
        return new ChatlistAdapter.viewholder(listitemview);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
            getusername(list.get(position),holder);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle=new Bundle();
                    bundle.putString("uid",list.get(holder.getAbsoluteAdapterPosition()));
                    navController.navigate(R.id.chatFragment,bundle);
                }
            });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewholder extends RecyclerView.ViewHolder{
        TextView username;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            username=itemView.findViewById(R.id.chatusername);
        }
    }
    public String getusername(String uid ,viewholder holder){
        database=FirebaseDatabase.getInstance();
        useref=database.getReference("App/user");
        useref.child(uid).child("username").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    username=snapshot.getValue(String.class);
                    holder.username.setText(username);


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return username;
    }

}
