package com.reemzet.chandan.fragment;

import android.app.DatePickerDialog;
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
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firestore.v1.StructuredQuery;
import com.reemzet.chandan.Adapter.CartViewHolder;
import com.reemzet.chandan.Adapter.MyorderViewholder;
import com.reemzet.chandan.Models.CartModel;
import com.reemzet.chandan.Models.OrderDetails;
import com.reemzet.chandan.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MyOrders extends Fragment {

    RecyclerView myorderrecycler;
    NavController navController;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference userorderref;
    Toolbar toolbar;
    FirebaseRecyclerAdapter<OrderDetails, MyorderViewholder> adapter;
    int d,m,y,minute,hours;
    Query query;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_my_orders, container, false);
        myorderrecycler=view.findViewById(R.id.myorderrecycyler);
        NavHostFragment navHostFragment = (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        assert navHostFragment != null;
        navController = navHostFragment.getNavController();

        myorderrecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        myorderrecycler.setLayoutManager(linearLayoutManager);

        mAuth= FirebaseAuth.getInstance();
        database= FirebaseDatabase.getInstance();
        userorderref=database.getReference("App/user").child(mAuth.getUid()).child("order");
        query=userorderref;
        toolbar = getActivity().findViewById(R.id.toolbar);
  /*      ImageView imageView=getActivity().findViewById(R.id.logout);
        imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_filter_list_24));
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c=Calendar.getInstance();
                y=c.get(Calendar.YEAR);
                m=c.get(Calendar.MONTH);
                d=c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dp=new DatePickerDialog(getActivity(), (view1, year, month, dayOfMonth) -> {
                    month++;
                   // tvtestdate.setText(dayOfMonth+"/"+month+"/"+year);

                },y,m,d);
                dp.setTitle("Select Date");
                dp.show();
            }
        });*/
        toolbar.setTitle("My Orders");
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.popBackStack(R.id.home2,true);
                NavHostFragment.findNavController(MyOrders.this).navigate(R.id.home2);
            }
        });



        setmyorderitem();



        return  view;

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
                        navController.navigate(R.id.action_myOrders_to_myOrderDetails,bundle);
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
       myorderrecycler.setAdapter(adapter);
        adapter.startListening();
    }
}