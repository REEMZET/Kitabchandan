package com.reemzet.chandan.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.CountDownTimer;
import android.os.Looper;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.reemzet.chandan.Adapter.CartViewHolder;
import com.reemzet.chandan.Adapter.OfferViewHolder;
import com.reemzet.chandan.Models.CartModel;
import com.reemzet.chandan.Models.OfferCode;
import com.reemzet.chandan.Models.OrderDetails;
import com.reemzet.chandan.Models.UserModel;
import com.reemzet.chandan.R;

import java.util.ArrayList;


public class CartFragment extends Fragment {

    RecyclerView cartrecyclerview;
    FirebaseDatabase database;
    DatabaseReference usercartrefe;
    FirebaseAuth mAuth;
    FirebaseRecyclerAdapter<CartModel,CartViewHolder> adapter;
    TextView tvcarttotal,tvcartvalue,tvdiscountvalue,tvpayableamount,tvname,tvadd,tvcity,tvmob,tvpincode,deliverycharges,btnappliedcode,tvchangeadd;
    int totalamount=0,delivercharges;
    Toolbar toolbar;
    NavController navController;
    ProgressDialog progressDialog;
    LinearLayout makepayement;
    FusedLocationProviderClient fusedlocclient;
    TextView tvdistance,tvdistanceexceed;
    LocationRequest locationRequest;
    boolean rangeavail;
    EditText etpromocode;
    String lati,longi;
    Double latitudeloc,longitudeloc;
    int discountprice;

    DatabaseReference userref;
    UserModel userModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view=inflater.inflate(R.layout.fragment_cart, container, false);
        cartrecyclerview=view.findViewById(R.id.cartrecyceler);
        tvcarttotal=view.findViewById(R.id.tvtotalcart);
        tvcartvalue=view.findViewById(R.id.tvcartvalue);
        tvdiscountvalue=view.findViewById(R.id.tvdiscount);
        tvadd=view.findViewById(R.id.tvaddress);
        tvname=view.findViewById(R.id.tvaddname);
        tvcity=view.findViewById(R.id.tvcity);
        tvmob=view.findViewById(R.id.tvmob);
        tvpincode=view.findViewById(R.id.tvpincode);
        tvpayableamount=view.findViewById(R.id.tvpayableamount);
        makepayement=view.findViewById(R.id.btnpayment);
        tvdistance=view.findViewById(R.id.tvdistance);
        tvdistanceexceed=view.findViewById(R.id.tvdistanceexceed);
        deliverycharges=view.findViewById(R.id.tvdeliverycharges);
        etpromocode=view.findViewById(R.id.etpromocode);
        tvchangeadd=view.findViewById(R.id.tvchangeadd);
        btnappliedcode=view.findViewById(R.id.tvapplypromo);
        latitudeloc=getArguments().getDouble("latitudeloc");
        longitudeloc=getArguments().getDouble("longitutdeloc");

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);


        fusedlocclient = LocationServices.getFusedLocationProviderClient(getActivity());
        NavHostFragment navHostFragment = (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        assert navHostFragment != null;
        navController = navHostFragment.getNavController();
        cartrecyclerview.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        mAuth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        userref=database.getReference("App/user");
        usercartrefe=database.getReference("App/user").child(mAuth.getUid()).child("usercart");


        checkcartitem();
        getuserdetails();
        getCurrentLocation();


        toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Cart");
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.popBackStack(R.id.home2,true);
                NavHostFragment.findNavController(CartFragment.this).navigate(R.id.home2);
            }
        });
        btnappliedcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etpromocode.getText().toString().isEmpty()){
                    Toast.makeText(getActivity(),"please enter promocode",Toast.LENGTH_SHORT).show();
                }else {
                    checkpromocode(etpromocode.getText().toString());
                    etpromocode.getText().clear();
                }
            }
        });
        tvchangeadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_cartFragment_to_userProfile);
            }
        });
        makepayement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             if (rangeavail){
                 setuserlocation();
                 Bundle bundle=new Bundle();
                 bundle.putString("totalprice",String.valueOf(totalamount+delivercharges+discountprice));
                    navController.navigate(R.id.action_cartFragment_to_paymentPage,bundle);
                    totalamount=0;
             }else {

                 getCurrentLocation();
                 Toast.makeText(getActivity(), "please refresh location", Toast.LENGTH_SHORT).show();
             }

            }
        });

        tvdistance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCurrentLocation();

            }
        });




        return view;
    }

    private void checkcartitem() {
        showloding();
        usercartrefe.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()){
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(),"No Any Item",Toast.LENGTH_SHORT).show();
                    makepayement.setEnabled(false);
                }else {
                    progressDialog.dismiss();
                    makepayement.setEnabled(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setcartitem() {

        totalamount=0;
        FirebaseRecyclerOptions<CartModel> options =
                new FirebaseRecyclerOptions.Builder<CartModel>()
                        .setQuery(usercartrefe, CartModel.class)
                        .build();
        adapter = new FirebaseRecyclerAdapter<CartModel, CartViewHolder>(options) {


            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cartlayout, parent, false);
                return new CartViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull CartModel model) {

                holder.itemname.setText(model.getItemname());
                holder.itemmrp.setPaintFlags(holder.itemmrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                holder.itemmrp.setText("mrp:" + String.valueOf(Integer.parseInt(model.getItemmrp()) * Integer.parseInt(model.getItemqty())));
                holder.itemqty.setText(model.getItemqty());
                holder.itemprice.setText("\u20B9" + String.valueOf(Integer.parseInt(model.getItemprice()) * Integer.parseInt(model.getItemqty())));

                Glide.with(getActivity())
                        .load(model.getItemimg())
                        .centerCrop()
                        .placeholder(R.drawable.books)
                        .into(holder.itemimg);


                holder.btnincrement.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String incr = String.valueOf(Integer.parseInt(model.getItemqty()) + 1);
                        usercartrefe.child(getRef(holder.getAdapterPosition()).getKey()).child("itemqty").setValue(incr).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                totalamount=0;
                                adapter.notifyDataSetChanged();
                            }
                        });

                    }
                });
                holder.btndecrement.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String decre = String.valueOf(Integer.parseInt(model.getItemqty()) - 1);
                        if (Integer.parseInt(decre) < 1) {
                            Toast.makeText(getActivity(), "can't be zero", Toast.LENGTH_SHORT).show();
                        } else {
                            usercartrefe.child(getRef(holder.getAdapterPosition()).getKey()).child("itemqty").setValue(decre).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    totalamount=0;
                                    adapter.notifyDataSetChanged();
                                }
                            });
                        }
                    }
                });
                holder.btndelete.setOnClickListener(v -> {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Alert!")
                            .setMessage("Are you about to remove an item from your cart.Are you sure to remove this item?")
                            .setPositiveButton(android.R.string.yes, (dialog, which) ->
                                    usercartrefe.child(getRef(holder.getAdapterPosition()).getKey()).removeValue()
                                            .addOnCompleteListener(task -> {
                                                totalamount=0;
                                                adapter.notifyDataSetChanged();
                                                    }
                                            ))
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                });
                int priceofoneitem=((Integer.parseInt(model.getItemprice())))*Integer.parseInt(model.getItemqty());
                totalamount = totalamount +priceofoneitem;
                tvcarttotal.setText(String.valueOf(("Total:" + "\u20B9" + totalamount)));
                tvcartvalue.setText("\u20B9"+totalamount);
                discountprice=Integer.parseInt(tvdiscountvalue.getText().toString());
                tvpayableamount.setText("\u20B9"+String.valueOf(totalamount+delivercharges+discountprice));

            }

        };

        cartrecyclerview.setAdapter(adapter);
        adapter.startListening();

    }


    public void showloding() {
        if(getActivity() != null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.show();
            progressDialog.setContentView(R.layout.dialoprogress);
            progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            progressDialog.setCanceledOnTouchOutside(false);
        }
    }


    public void setdeliverycharge(){

        DatabaseReference storedetails= database.getReference("App/storedetails");
        storedetails.child("deliverycharges").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    deliverycharges.setText(snapshot.getValue(String.class));
                    delivercharges=Integer.parseInt(snapshot.getValue(String.class));
                    setcartitem();
                }else {
                    deliverycharges.setText(String.valueOf(+40));
                    setcartitem();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void checkpromocode(String code){
        showloding();
        DatabaseReference offer=database.getReference("App/offer");
        offer.orderByChild("promocode").startAt(code).endAt(code+ "\uf8ff").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (snapshot.exists()){
                for (DataSnapshot items : snapshot.getChildren()){
                    tvdiscountvalue.setText("-"+items.child("discountpercent").getValue().toString());
                    Toast.makeText(getActivity(),"Promocode applied",Toast.LENGTH_SHORT).show();
                    totalamount=0;
                    adapter.notifyDataSetChanged();
                    progressDialog.dismiss();
                }

            }else {
                tvdiscountvalue.setText("-0");
                Toast.makeText(getActivity(),"Promocode do not exit",Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getuserdetails(){
        userref.child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                userModel=snapshot.getValue(UserModel.class);
                tvadd.setText(userModel.getUseraddress());
                tvmob.setText(userModel.getUserphone());
                tvcity.setText(userModel.getUsercity());
                tvpincode.setText(userModel.getPincode());
                tvname.setText(userModel.getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }





    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){

                if (isGPSEnabled()) {

                    getCurrentLocation();

                }else {

                    turnOnGPS();
                }
            }
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {

                getCurrentLocation();
            }
        }
    }


    private void getCurrentLocation() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                if (isGPSEnabled()) {

                    LocationServices.getFusedLocationProviderClient(getActivity())
                            .requestLocationUpdates(locationRequest, new LocationCallback() {
                                @Override
                                public void onLocationResult(@NonNull LocationResult locationResult) {
                                    super.onLocationResult(locationResult);
                                    Context context=getActivity();
                                    if (context!=null){
                                        LocationServices.getFusedLocationProviderClient(getActivity())
                                                .removeLocationUpdates(this);
                                    }


                                    if (locationResult != null && locationResult.getLocations().size() >0){

                                        int index = locationResult.getLocations().size() - 1;
                                        double latitude = locationResult.getLocations().get(index).getLatitude();
                                        double longitude = locationResult.getLocations().get(index).getLongitude();

                                        Location userlocation=new Location("locationprovider");
                                        userlocation.setLongitude(longitude);
                                        userlocation.setLatitude(latitude);
                                        lati=String.valueOf(latitude);
                                        longi=String.valueOf(longitude);

                                        Location storelocation=new Location("location2");
                                        storelocation.setLatitude(latitudeloc);
                                        storelocation.setLongitude(longitudeloc);
                                        float fixeddistance=8f;
                                        float userlocationfromstore=userlocation.distanceTo(storelocation)/1000;

                                        tvdistance.setText("="+String.valueOf(userlocation.distanceTo(storelocation)/1000)+"KM");
                                        if (userlocationfromstore>fixeddistance){
                                            rangeavail=false;
                                            tvdistanceexceed.setVisibility(View.VISIBLE);
                                            tvdistanceexceed.setText("sorry we can not reach at you because our facility only available  in 8km");
                                            tvdistance.setText("="+String.valueOf(userlocation.distanceTo(storelocation)/1000)+"Km");
                                        }else {
                                            rangeavail=true;
                                        }


                                    }
                                }
                            }, Looper.getMainLooper());

                } else {
                    turnOnGPS();
                }

            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }


    private void turnOnGPS() {

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(getActivity())
                .checkLocationSettings(builder.build());

        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {

                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    Toast.makeText(getActivity(), "GPS is already tured on", Toast.LENGTH_SHORT).show();

                } catch (ApiException e) {

                    switch (e.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                            try {
                                ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                                resolvableApiException.startResolutionForResult(getActivity(), 2);
                            } catch (IntentSender.SendIntentException ex) {
                                ex.printStackTrace();
                            }
                            break;

                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            //Device does not have location
                            break;
                    }
                }
            }
        });

    }

    public void setuserlocation(){
        userref.child(mAuth.getUid()).child("userlocation").setValue(lati+","+longi);
    }





    private boolean isGPSEnabled() {
        LocationManager locationManager = null;
        boolean isEnabled = false;

        if (locationManager == null) {
            locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        }

        isEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isEnabled;

    }

    @Override
    public void onStart() {
        super.onStart();
        setdeliverycharge();
    }
}