package com.reemzet.chandan.adminfragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.reemzet.chandan.Adapter.AdminCatListAdapter;
import com.reemzet.chandan.Adapter.CategorylistAdapter;
import com.reemzet.chandan.R;
import com.reemzet.chandan.fragment.ListofCategory;

import java.util.ArrayList;

public class AdminCatList extends Fragment {

   RecyclerView admincatlistrecyclerview;
    FirebaseDatabase database;
    DatabaseReference listofcatref;
    ArrayList<String> catlist;
    NavController navController;
    AdminCatListAdapter adapter;
    Toolbar toolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view= inflater.inflate(R.layout.fragment_admin_cat_list, container, false);
       admincatlistrecyclerview=view.findViewById(R.id.admincatlist);
        database=FirebaseDatabase.getInstance();
        listofcatref=database.getReference("App/Items");
        NavHostFragment navHostFragment = (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        assert navHostFragment != null;
        navController = navHostFragment.getNavController();
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL);
        admincatlistrecyclerview.setLayoutManager(staggeredGridLayoutManager);


        catlist=new ArrayList<>();
        listofcatref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()){
                    if (!ds.getKey().equals("itemlist")){
                        catlist.add(ds.getKey());
                    }
                    adapter=new AdminCatListAdapter(getActivity(),catlist,navController);
                    admincatlistrecyclerview.setAdapter(adapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Category");
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.popBackStack(R.id.adminHome,true);
                NavHostFragment.findNavController(AdminCatList.this).navigate(R.id.adminHome);
            }
        });


       return view;
    }
}