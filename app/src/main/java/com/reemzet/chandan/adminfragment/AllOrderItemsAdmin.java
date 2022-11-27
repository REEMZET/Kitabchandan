package com.reemzet.chandan.adminfragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.reemzet.chandan.Adapter.MyorderViewholder;
import com.reemzet.chandan.Models.OrderDetails;
import com.reemzet.chandan.R;
import com.reemzet.chandan.fragment.UserProfile;

import java.util.ArrayList;

public class AllOrderItemsAdmin extends Fragment {

    RecyclerView allorderrecycler;
    NavController navController;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference adminorderref;
    Query query;
    TextView tvcallbtn;

    Toolbar toolbar;

    FirebaseRecyclerAdapter<OrderDetails, MyorderViewholder> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_all_order_items_admin, container, false);
        allorderrecycler=view.findViewById(R.id.allorderadminrecycler);



        NavHostFragment navHostFragment = (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        assert navHostFragment != null;
        navController = navHostFragment.getNavController();

        allorderrecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        allorderrecycler.setLayoutManager(linearLayoutManager);

        mAuth= FirebaseAuth.getInstance();
        database= FirebaseDatabase.getInstance();
        adminorderref=database.getReference("App/Admincart");
        query=adminorderref;
        setmyorderitem();
        toolbar=getActivity().findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setTitle("All Orders");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.popBackStack(R.id.adminHome, true);
                NavHostFragment.findNavController(AllOrderItemsAdmin.this).navigate(R.id.adminHome);
            }
        });




  return view;
    }

    private void setmyorderitem() {
        FirebaseRecyclerOptions<OrderDetails> options =
                new FirebaseRecyclerOptions.Builder<OrderDetails>()
                        .setQuery(query, OrderDetails.class)
                        .build();

        adapter = new FirebaseRecyclerAdapter<OrderDetails, MyorderViewholder>(options) {

            @NonNull
            @Override
            public MyorderViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.myorderlayout, parent, false);
                return new MyorderViewholder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull MyorderViewholder holder, int position, @NonNull OrderDetails model) {
                holder.orderid.setText(model.getOrderid());
                holder.orderdate.setText(model.getOrderdate());
                holder.orderstatus.setText(model.getOrderstatus());
                holder.ordertotalprice.setText("\u20B9"+model.getOrdertotalprice());
                holder.ordertime.setText(model.getOrdertime());
                holder.paymentoption.setText(model.getPaymentoption());
                holder.username.setText(model.getUsername());
                holder.viewmore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle bundle=new Bundle();
                        bundle.putString("orderid",model.getOrderid());
                        bundle.putString("useruid",model.getUserid());
                        navController.navigate(R.id.adminOrderDetails,bundle);
                    }
                });

                String status=model.getOrderstatus();

                switch (status){
                    case "Pending":
                        holder.orderstatus.setTextColor(Color.RED);
                        break;
                    case "Confirm":
                        holder.orderstatus.setTextColor(getResources().getColor(R.color.yellow));
                        break;
                    case "Out for Delivery":
                        holder.orderstatus.setTextColor(getResources().getColor(R.color.skyblue));
                        break;
                    case "Delivered":
                        holder.orderstatus.setTextColor(getResources().getColor(R.color.teal_700));
                        break;
                }
            }
        };
        allorderrecycler.setAdapter(adapter);
        adapter.startListening();
    }


}