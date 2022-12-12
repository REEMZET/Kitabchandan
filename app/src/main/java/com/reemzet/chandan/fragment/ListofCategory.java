package com.reemzet.chandan.fragment;

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
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.reemzet.chandan.Adapter.CategorylistAdapter;
import com.reemzet.chandan.R;

import java.util.ArrayList;


public class  ListofCategory extends Fragment {

    RecyclerView  categorylistrecycler;
    FirebaseDatabase database;
    DatabaseReference listofcatref;
    ArrayList<String> catlist;
    NavController navController;
    CategorylistAdapter adapter;
    Toolbar toolbar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_listof_category, container, false);

        database=FirebaseDatabase.getInstance();
        listofcatref=database.getReference("App/Items");
        categorylistrecycler=view.findViewById(R.id.categorylistrecycler);
        NavHostFragment navHostFragment = (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        assert navHostFragment != null;
        navController = navHostFragment.getNavController();
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL);
        categorylistrecycler.setLayoutManager(staggeredGridLayoutManager);


        catlist=new ArrayList<>();
        listofcatref.orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()){
                    if (!ds.getKey().equals("itemlist")){
                        catlist.add(ds.getKey());
                    }
                adapter=new CategorylistAdapter(getActivity(),catlist,navController);
                    categorylistrecycler.setAdapter(adapter);

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
                navController.popBackStack(R.id.home2,true);
                NavHostFragment.findNavController(ListofCategory.this).navigate(R.id.home2);
            }
        });

    return view;


    }
}