package com.reemzet.chandan.adminfragment;

import android.app.ProgressDialog;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.PropertyName;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.reemzet.chandan.R;
import com.reemzet.chandan.fragment.CartFragment;


public class StoreDetails extends Fragment {


        FirebaseDatabase database;
        DatabaseReference storedetailsref,updateref;
        TextView tvdeliverycharges,tvlocation,tvchangedelivery,tvchangelocation;
        Toolbar toolbar;
        NavController navController;
        Switch switchupdate;
        ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_store_details, container, false);

        database=FirebaseDatabase.getInstance();
        storedetailsref=database.getReference("App/storedetails");
        updateref=database.getReference("App/update");
        tvchangedelivery=view.findViewById(R.id.tvchangedeliverycharges);
        tvchangelocation=view.findViewById(R.id.tvchangelocation);
        tvdeliverycharges=view.findViewById(R.id.tvdelliverycharge);
        tvlocation=view.findViewById(R.id.tvlocation);


        NavHostFragment navHostFragment = (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        assert navHostFragment != null;
        navController = navHostFragment.getNavController();
        toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Store Details");
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.popBackStack(R.id.adminHome,true);
                NavHostFragment.findNavController(StoreDetails.this).navigate(R.id.adminHome);
            }
        });

        storedetailsref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String charges=snapshot.child("deliverycharges").getValue(String.class);
                tvdeliverycharges.setText("Rs."+charges);
                tvlocation.setText(snapshot.child("location/latitude").getValue(String.class)+","+snapshot.child("location/longitude").getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        tvchangedelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogPlus dialogPlus = DialogPlus.newDialog(getActivity())
                        .setContentHolder(new ViewHolder(R.layout.deliverychargelayout))
                        .setGravity(Gravity.CENTER)
                        .setCancelable(false)
                        .create();
                dialogPlus.show();
                View dialogview = dialogPlus.getHolderView();
                EditText etdeliveryprice=dialogview.findViewById(R.id.etdelivercharges);
                Button deliveryset=dialogview.findViewById(R.id.deliverychargeset);
                ImageView closebtn=dialogview.findViewById(R.id.closebtn);
                deliveryset.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (etdeliveryprice.getText().toString().isEmpty()){
                            etdeliveryprice.setError("invalid");
                            etdeliveryprice.requestFocus();
                        }else {
                            storedetailsref.child("deliverycharges").setValue("+"+etdeliveryprice.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                dialogPlus.dismiss();
                                }
                            });
                        }
                    }
                });
                closebtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                dialogPlus.dismiss();
                    }
                });
            }
        });
        tvchangelocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogPlus dialogPlus = DialogPlus.newDialog(getActivity())
                        .setContentHolder(new ViewHolder(R.layout.setstorelocation))
                        .setGravity(Gravity.CENTER)
                        .setCancelable(false)
                        .create();
                dialogPlus.show();
                View dialogview = dialogPlus.getHolderView();
                EditText etlongtude=dialogview.findViewById(R.id.etlongtitude);
                EditText etlatitude=dialogview.findViewById(R.id.etlatitude);
                Button deliveryset=dialogview.findViewById(R.id.deliverychargeset);
                ImageView closebtn=dialogview.findViewById(R.id.closebtn);
                deliveryset.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (etlatitude.getText().toString().isEmpty()){
                            etlatitude.setError("invalid");
                            etlatitude.requestFocus();
                        }else if (etlongtude.getText().toString().isEmpty())
                        {
                            etlongtude.setError("invalid");
                            etlongtude.requestFocus();
                        }else {
                            storedetailsref.child("location").child("latitude").setValue(etlatitude.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    storedetailsref.child("location").child("longitude").setValue(etlongtude.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            dialogPlus.dismiss();
                                        }
                                    });
                                }
                            });
                        }
                    }
                });
                closebtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogPlus.dismiss();
                    }
                });
            }
        });


   return view;
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


}