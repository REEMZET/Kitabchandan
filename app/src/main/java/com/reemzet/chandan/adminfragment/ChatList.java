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
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.reemzet.chandan.Adapter.AdminCatListAdapter;
import com.reemzet.chandan.Adapter.ChatlistAdapter;
import com.reemzet.chandan.ChatFragment;
import com.reemzet.chandan.R;

import java.util.ArrayList;


public class ChatList extends Fragment {
    ArrayList<String> list;
    ChatlistAdapter adapter;
    RecyclerView chatlistrecyclerview;
    FirebaseDatabase database;
    DatabaseReference chatlistref,useref;
    Toolbar toolbar;
    NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_chat_list, container, false);
        chatlistrecyclerview=view.findViewById(R.id.chatlistrecyclerview);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL);
        chatlistrecyclerview.setLayoutManager(staggeredGridLayoutManager);
        NavHostFragment navHostFragment = (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        assert navHostFragment != null;
        navController = navHostFragment.getNavController();
        database=FirebaseDatabase.getInstance();
        chatlistref=database.getReference("App/chat");
        useref=database.getReference("App/user");
        toolbar=getActivity().findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setTitle("Conversations");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.popBackStack(R.id.adminHome, true);
                NavHostFragment.findNavController(ChatList.this).navigate(R.id.adminHome);
            }
        });
        list=new ArrayList<>();
        chatlistref.orderByKey().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()) {
                      list.add(ds.getKey());
                    adapter=new ChatlistAdapter(getActivity(),list,navController);
                    chatlistrecyclerview.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }

}