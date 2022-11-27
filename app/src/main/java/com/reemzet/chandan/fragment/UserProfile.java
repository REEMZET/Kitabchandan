package com.reemzet.chandan.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
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
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.reemzet.chandan.Models.UserModel;
import com.reemzet.chandan.R;

import java.util.HashMap;

public class UserProfile extends Fragment {

    FirebaseDatabase database;
    DatabaseReference userprofileref,storedetailsref;
    FirebaseAuth mAuth;
    UserModel userModel;
    TextView username, useraddd, name, phone;
    LinearLayout myorderbtn, cartbtn;
    NavController navController;
    TextView tvpicode, tvcity, tvmob;
    Toolbar toolbar;
    ImageView editprofile;
    ProgressDialog progressDialog;
    EditText editprofilename, etprofulladd, etpropincode, etprocity;
    TextView tvcancel, tvsubmit;
    String latitude,longititude;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        userprofileref = database.getReference("App/user");
        storedetailsref=database.getReference("App/storedetails");
        username = view.findViewById(R.id.profileusername);
        useraddd = view.findViewById(R.id.useraddress);
        name = view.findViewById(R.id.profilename);
        phone = view.findViewById(R.id.userphone);
        myorderbtn = view.findViewById(R.id.myorderbtn);
        tvcity = view.findViewById(R.id.tvusercity2);
        tvpicode = view.findViewById(R.id.tvuserpincode2);
        tvmob = view.findViewById(R.id.tvusermob2);
        cartbtn = view.findViewById(R.id.cartbtn);
        toolbar = getActivity().findViewById(R.id.toolbar);
        editprofile = view.findViewById(R.id.editprofile);

        NavHostFragment navHostFragment = (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        assert navHostFragment != null;
        navController = navHostFragment.getNavController();
        setprofiledata();
        myorderbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.myOrders);
            }
        });
        cartbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                statusCheck();

            }
        });

        getstorelocation();
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setTitle("My profile");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.popBackStack(R.id.home2, true);
                NavHostFragment.findNavController(UserProfile.this).navigate(R.id.home2);
            }
        });
        editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogPlus dialogPlus = DialogPlus.newDialog(getActivity())
                        .setContentHolder(new ViewHolder(R.layout.editaddress))
                        .setGravity(Gravity.CENTER)
                        .setCancelable(false)
                        .create();
                dialogPlus.show();
                View dialogview = dialogPlus.getHolderView();
                editprofilename =  dialogview.findViewById(R.id.editprofilename);
                etprofulladd = dialogview.findViewById(R.id.etfulladd);
                etpropincode = dialogview.findViewById(R.id.etpropincode);
                etprocity = dialogview.findViewById(R.id.etprocity);
                tvcancel = dialogview.findViewById(R.id.tvcancel);
                tvsubmit = dialogview.findViewById(R.id.tvsubmitprofile);

                tvcancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogPlus.dismiss();
                    }
                });
          tvsubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (editprofilename.getText().toString().isEmpty()){
                            editprofilename.setError("empty");
                            editprofilename.requestFocus();
                        }else if (etprofulladd.getText().toString().isEmpty()){
                            etprofulladd.setError("empty");
                            etprofulladd.requestFocus();

                        }else if (etprocity.getText().toString().isEmpty()){
                            etprocity.setError("empty");
                            etprocity.requestFocus();
                        }else if (etpropincode.getText().toString().isEmpty()){
                            etpropincode.setError("empty");
                            etpropincode.requestFocus();
                        }else {
                            showloding();
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("username",editprofilename.getText().toString());
                            map.put("useraddress",etprofulladd.getText().toString());
                            map.put("usercity",etprocity.getText().toString());
                            map.put("pincode",etpropincode.getText().toString());
                            userprofileref.child(mAuth.getUid()).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    progressDialog.dismiss();
                                    dialogPlus.dismiss();
                                    setprofiledata();
                                }
                            });
                        }
                    }
                });

            }
        });


        return view;
    }


    public void getstorelocation(){

    }
    private void setprofiledata() {
        userprofileref.child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userModel = snapshot.getValue(UserModel.class);
                username.setText(userModel.getUsername());
                name.setText(userModel.getUsername());
                phone.setText(userModel.getUserphone());
                useraddd.setText(userModel.getUseraddress());
                tvcity.setText("City:-" + userModel.getUsercity());
                tvpicode.setText("Pin:-" + userModel.getPincode());
                tvmob.setText("Mob:-" + userModel.getUserphone());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void showloding() {
        if (getActivity() != null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.show();
            progressDialog.setContentView(R.layout.dialoprogress);
            progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            progressDialog.setCanceledOnTouchOutside(false);
        }
    }


    public void statusCheck() {
        final LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        } else {
            Dexter.withContext(getActivity())
                    .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    .withListener(new PermissionListener() {
                        @Override
                        public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                            storedetailsref.child("location").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()){
                                        latitude=snapshot.child("latitude").getValue(String.class);
                                        longititude=snapshot.child("longitude").getValue(String.class);
                                        Bundle bundle=new Bundle();
                                        bundle.putDouble("latitudeloc",Double.valueOf(latitude));
                                        bundle.putDouble("longitutdeloc",Double.valueOf(longititude));
                                        navController.navigate(R.id.cartFragment,bundle);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        }

                        @Override
                        public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                            Toast.makeText(getActivity(), "you can not access cart without location", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                            permissionToken.continuePermissionRequest();
                        }
                    }).check();
        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
}