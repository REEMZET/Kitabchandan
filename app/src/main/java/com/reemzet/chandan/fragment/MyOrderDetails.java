package com.reemzet.chandan.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.reemzet.chandan.Adapter.MoreOrderDetailsAdapter;
import com.reemzet.chandan.Models.CartModel;
import com.reemzet.chandan.Models.OrderDetails;
import com.reemzet.chandan.Models.UserModel;
import com.reemzet.chandan.R;

import java.util.ArrayList;
import java.util.List;

public class MyOrderDetails extends Fragment {

    RecyclerView myorderdetailsrecycler;
    String orderid;
    FirebaseDatabase database;
    DatabaseReference userorderref,userref
            ;
    FirebaseAuth mAuth;
    UserModel userModel;
    TextView tvadd,tvname,tvmob,tvcity,tvpincode;

    List<CartModel> cartModelArrayList;
    Toolbar toolbar;
    NavController navController;
    View pendingview,confirmview,outfordeliveryview,deliveredview,pendingtoconfirmview,confirmtooutview,outtodeliveredview;
OrderDetails orderDetails;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_order_details, container, false);
        myorderdetailsrecycler = view.findViewById(R.id.myorderdetailsrecycler);
        tvadd=view.findViewById(R.id.tvuseraddress);
        tvmob=view.findViewById(R.id.tvusermob);
        tvcity=view.findViewById(R.id.tvusercity);
        tvpincode=view.findViewById(R.id.tvuserpincode);
        tvname=view.findViewById(R.id.tvusertname);
        pendingview=view.findViewById(R.id.pendingview);
        confirmview=view.findViewById(R.id.confirmview);
        outfordeliveryview=view.findViewById(R.id.outfordeliveryview);
        deliveredview=view.findViewById(R.id.deliveredview);
        pendingtoconfirmview=view.findViewById(R.id.pendingtoconfirmview);
        confirmtooutview=view.findViewById(R.id.confirmtooutview);
        outtodeliveredview=view.findViewById(R.id.outtodeliveredview);

        NavHostFragment navHostFragment = (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        assert navHostFragment != null;
        navController = navHostFragment.getNavController();


        orderid = getArguments().getString("orderid");
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        database=FirebaseDatabase.getInstance();
        userref=database.getReference("App/user");
        userorderref = database.getReference("App/user").child(mAuth.getUid()).child("order").child(orderid);

        myorderdetailsrecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        cartModelArrayList=new ArrayList<>();
        toolbar=getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Order Details");
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.popBackStack(R.id.home2,true);
                NavHostFragment.findNavController(MyOrderDetails.this).navigate(R.id.home2);
            }
        });


        userorderref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orderDetails=snapshot.getValue(OrderDetails.class);
                changedeliverystatus();
                for (DataSnapshot item : snapshot.getChildren()) {
                    switch (item.getKey()) {
                        case "orderid":
                        case "orderdate":
                        case "paymentoption":
                        case "ordertime":
                        case "ordertitle":
                        case "orderqty":
                        case "ordertotalprice":
                        case "orderstatus":
                        case "deliveryaddress":
                        case "deliverylocation":
                        case "username":
                        case "userphone":
                        case "userid":
                        case "userdeviceid":
                            break;
                        default:
                            CartModel cartModel=item.getValue(CartModel.class);
                            cartModelArrayList.add(cartModel);

                    }
                }
                myorderdetailsrecycler.setAdapter(new MoreOrderDetailsAdapter(getActivity(),cartModelArrayList));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        getuserdetails();
        return view;
    }

    private void changedeliverystatus() {
        String status=orderDetails.getOrderstatus();

        switch (status){

            case "Pending":
                pendingview.setVisibility(View.VISIBLE);
                break;
            case "Confirm":
                pendingview.setVisibility(View.VISIBLE);
                pendingtoconfirmview.setVisibility(View.VISIBLE);
                confirmview.setVisibility(View.VISIBLE);
                break;
            case "Out for Delivery":
                pendingview.setVisibility(View.VISIBLE);
                pendingtoconfirmview.setVisibility(View.VISIBLE);
                confirmview.setVisibility(View.VISIBLE);
                confirmtooutview.setVisibility(View.VISIBLE);
                outfordeliveryview.setVisibility(View.VISIBLE);
                break;
            case "Delivered":
                pendingview.setVisibility(View.VISIBLE);
                pendingtoconfirmview.setVisibility(View.VISIBLE);
                confirmview.setVisibility(View.VISIBLE);
                confirmtooutview.setVisibility(View.VISIBLE);
                outfordeliveryview.setVisibility(View.VISIBLE);
                outtodeliveredview.setVisibility(View.VISIBLE);
                outtodeliveredview.setVisibility(View.VISIBLE);
                deliveredview.setVisibility(View.VISIBLE);
        }
    }

    public void getuserdetails(){
        userref.child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userModel=snapshot.getValue(UserModel.class);
                tvadd.setText("Add:- "+userModel.getUseraddress());
                tvmob.setText("Mob:- "+userModel.getUserphone());
                tvcity.setText("City:- "+userModel.getUsercity());
                tvpincode.setText("Pincode:- "+userModel.getPincode());
                tvname.setText(userModel.getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}