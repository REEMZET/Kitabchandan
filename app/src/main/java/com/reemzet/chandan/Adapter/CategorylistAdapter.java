package com.reemzet.chandan.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.FirebaseDatabase;
import com.reemzet.chandan.R;
import com.reemzet.chandan.fragment.ListofCategory;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class CategorylistAdapter extends RecyclerView.Adapter<CategorylistAdapter.Viewholder> {

    Context context;
    ArrayList<String> arrayList;
    NavController navController;
    FirebaseDatabase database;

    public CategorylistAdapter(Context context, ArrayList<String> arrayList, NavController navController) {
        this.context = context;
        this.arrayList = arrayList;
        this.navController = navController;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View listitemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.categorylistlayout, parent, false);
        return new Viewholder(listitemview);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {

        holder.tvcatname.setText(arrayList.get(position));
        holder.listlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putString("category",holder.tvcatname.getText().toString());
                navController.navigate(R.id.action_listofCategory_to_categoryPage,bundle);

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
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            listlayout=itemView.findViewById(R.id.listlayout);
            tvcatname=itemView.findViewById(R.id.tvcatname);
        }
    }

}
