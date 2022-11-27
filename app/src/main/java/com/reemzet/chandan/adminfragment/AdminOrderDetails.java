package com.reemzet.chandan.adminfragment;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.onesignal.OneSignal;
import com.reemzet.chandan.Adapter.MoreOrderDetailsAdapter;
import com.reemzet.chandan.Models.CartModel;
import com.reemzet.chandan.Models.OrderDetails;
import com.reemzet.chandan.Models.UserModel;
import com.reemzet.chandan.R;
import com.reemzet.chandan.fragment.MyOrderDetails;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class AdminOrderDetails extends Fragment {

    RecyclerView adminorderrecyclerview;
    String orderid;
    FirebaseDatabase database;
    DatabaseReference userorderref,userref,adminorderref;
    FirebaseAuth mAuth;
    UserModel userModel;
    TextView tvadd,tvname,tvmob,tvcity,tvpincode,tvgetdirection;
    List<CartModel> cartModelArrayList;
    Toolbar toolbar;
    NavController navController;
    RadioButton rdpendingview,rdconfirmview,rdoutfordeliveryview,rddeliveredview;
    View pendingtoconfirmview,confirmtooutview,outtodeliveredview;
    OrderDetails orderDetails;
    String useruid;
    MoreOrderDetailsAdapter moreOrderDetailsAdapter;
    TextView tvcallbtn;
    String phone;
    String uri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
     View view= inflater.inflate(R.layout.fragment_admin_order_details, container, false);
        adminorderrecyclerview = view.findViewById(R.id.adminorderdetailsrecycler);
        tvcallbtn=view.findViewById(R.id.tvcall);
        tvadd=view.findViewById(R.id.tvuseraddress);
        tvmob=view.findViewById(R.id.tvusermob);
        tvcity=view.findViewById(R.id.tvusercity);
        tvpincode=view.findViewById(R.id.tvuserpincode);
        tvname=view.findViewById(R.id.tvusertname);
        rdpendingview=view.findViewById(R.id.pendingview);
        rdconfirmview=view.findViewById(R.id.confirmview);
        rdoutfordeliveryview=view.findViewById(R.id.outfordeliveryview);
        rddeliveredview=view.findViewById(R.id.deliveredview);
        pendingtoconfirmview=view.findViewById(R.id.pendingtoconfirmview);
        confirmtooutview=view.findViewById(R.id.confirmtooutview);
        outtodeliveredview=view.findViewById(R.id.outtodeliveredview);
        tvgetdirection=view.findViewById(R.id.tvgetdirection);


        NavHostFragment navHostFragment = (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        assert navHostFragment != null;
        navController = navHostFragment.getNavController();


        orderid = getArguments().getString("orderid");
        useruid=getArguments().getString("useruid");
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        database=FirebaseDatabase.getInstance();
        userref=database.getReference("App/user");
        userorderref = database.getReference("App/user").child(useruid).child("order").child(orderid);
        adminorderref=database.getReference("App/Admincart").child(orderid);

        adminorderrecyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        cartModelArrayList=new ArrayList<>();
        toolbar=getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Order Details");
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.popBackStack(R.id.adminHome,true);
                NavHostFragment.findNavController(AdminOrderDetails.this).navigate(R.id.adminHome);
            }
        });

        tvcallbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    Intent my_callIntent = new Intent(Intent.ACTION_DIAL);
                    my_callIntent.setData(Uri.parse("tel:"+phone));
                    //here the word 'tel' is important for making a call...
                    startActivity(my_callIntent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getActivity(), "Error in your phone call"+e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });



        adminorderref.addListenerForSingleValueEvent(new ValueEventListener() {
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
                moreOrderDetailsAdapter=new MoreOrderDetailsAdapter(getActivity(),cartModelArrayList);
               adminorderrecyclerview.setAdapter(moreOrderDetailsAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        getuserdetails();

        rdpendingview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adminorderref.child("orderstatus").setValue("Pending");
                userorderref.child("orderstatus").setValue("Pending");
                setnotification("Now Pending");

            }
        });
        rdconfirmview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adminorderref.child("orderstatus").setValue("Confirm");
                userorderref.child("orderstatus").setValue("Confirm");
                setnotification("Confirm now");

            }
        });
        rdoutfordeliveryview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adminorderref.child("orderstatus").setValue("Out for Delivery");
               userorderref.child("orderstatus").setValue("Out for Delivery");
                setnotification("Out for delivery");

            }
        });
        rddeliveredview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adminorderref.child("orderstatus").setValue("Delivered");
                userorderref.child("orderstatus").setValue("Delivered");

                setnotification("Delivered");
            }
        });

        tvgetdirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                getActivity().startActivity(intent);
            }
        });


    return view;
    }
    private void changedeliverystatus() {
        String status=orderDetails.getOrderstatus();
        switch (status){
            case "Pending":
                rdpendingview.setChecked(true);
                break;
            case "Confirm":
                rdpendingview.setChecked(true);
                pendingtoconfirmview.setVisibility(View.VISIBLE);

                rdconfirmview.setChecked(true);
                break;
            case "Out for Delivery":
                rdpendingview.setChecked(true);
                pendingtoconfirmview.setVisibility(View.VISIBLE);
                rdconfirmview.setChecked(true);
                confirmtooutview.setVisibility(View.VISIBLE);
                rdoutfordeliveryview.setChecked(true);
                break;
            case "Delivered":
                rdpendingview.setChecked(true);
                pendingtoconfirmview.setVisibility(View.VISIBLE);
                rdconfirmview.setChecked(true);
                confirmtooutview.setVisibility(View.VISIBLE);
                rdoutfordeliveryview.setChecked(true);
                outtodeliveredview.setVisibility(View.VISIBLE);
                rddeliveredview.setChecked(true);
        }
    }

    public void setnotification(String status){
        try {
            OneSignal.postNotification(new JSONObject("{'contents': {'en': '"+ "Your order is "+status+"" + ".now you can check status in my order section "+"'}," + " 'include_player_ids': ['" +userModel.getDeviceid()+ "']}"),
                    new OneSignal.PostNotificationResponseHandler() {
                        @Override
                        public void onSuccess(JSONObject response) {

                        }

                        @Override
                        public void onFailure(JSONObject response) {

                        }
                    });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getuserdetails(){
        userref.child(useruid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userModel=snapshot.getValue(UserModel.class);
                tvadd.setText("Add:- "+userModel.getUseraddress());
                tvmob.setText("Mob:- "+userModel.getUserphone());
                tvcity.setText("City:- "+userModel.getUsercity());
                tvpincode.setText("Pincode:- "+userModel.getPincode());
                tvname.setText(userModel.getUsername());
                phone=userModel.getUserphone();
                uri = "http://maps.google.com/maps?q=loc:" + userModel.getUserlocation() + " (" +userModel.getUsername()+"'s location"+ ")";
                tvgetdirection.setText("Get Direction");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}