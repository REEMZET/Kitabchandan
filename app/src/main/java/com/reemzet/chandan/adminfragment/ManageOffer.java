package com.reemzet.chandan.adminfragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.reemzet.chandan.Adapter.OfferViewHolder;
import com.reemzet.chandan.Models.OfferCode;
import com.reemzet.chandan.R;

public class ManageOffer extends Fragment {

        DatabaseReference offerref;
        ShimmerFrameLayout shimmeroffer;
        RecyclerView offerrecycler;
        Toolbar toolbar;
        FirebaseRecyclerAdapter<OfferCode, OfferViewHolder>adapter;
       FloatingActionButton createofferbtn;
        EditText etoffertitle,etofferdiscount,etpromocode;
        String offertitle,discount,promocode;
        TextView submitbtn,cancelbtn;
        ProgressDialog progressDialog;
        NavController navController;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_manage_offer, container, false);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database=FirebaseDatabase.getInstance();
        offerref=database.getReference("App").child("offer");
        shimmeroffer=view.findViewById(R.id.shimmeroffer);
        offerrecycler=view.findViewById(R.id.offerrecycler);
        createofferbtn=view.findViewById(R.id.floatingActionButton);
        NavHostFragment navHostFragment = (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        assert navHostFragment != null;
        navController = navHostFragment.getNavController();

        LinearLayoutManager offerlinearLayoutManager = new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false);

        offerrecycler.setLayoutManager(offerlinearLayoutManager);
        setofferdata();
        toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Offer zone");
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.popBackStack(R.id.adminHome,true);
                NavHostFragment.findNavController(ManageOffer.this).navigate(R.id.adminHome);
            }
        });

        createofferbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogPlus dialogPlus = DialogPlus.newDialog(getActivity())
                        .setContentHolder(new ViewHolder(R.layout.offercreatelayout))
                        .setGravity(Gravity.CENTER)
                        .setCancelable(false)
                        .create();
                dialogPlus.show();
                View dialogview = dialogPlus.getHolderView();
                etoffertitle=dialogview.findViewById(R.id.etoffertitle);
                etofferdiscount=dialogview.findViewById(R.id.etdiscountprice);
                etpromocode=dialogview.findViewById(R.id.etpromocode);
                submitbtn=dialogview.findViewById(R.id.tvsubmitoffer);
                cancelbtn=dialogview.findViewById(R.id.tvcancel);
                cancelbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogPlus.dismiss();
                    }
                });
                submitbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (etoffertitle.getText().toString().isEmpty()){
                            etoffertitle.setError("can't empty");
                            etoffertitle.requestFocus();
                        }else if (etofferdiscount.getText().toString().isEmpty()){
                            etofferdiscount.setError("can't empty");
                            etofferdiscount.requestFocus();

                        }else if (etpromocode.getText().toString().isEmpty()){
                            etpromocode.setError("can't empty");
                            etpromocode.requestFocus();
                        }else {
                            showloding();
                            offertitle=etoffertitle.getText().toString();
                            discount=etofferdiscount.getText().toString();
                            promocode=etpromocode.getText().toString();
                            OfferCode offerCode=new OfferCode(offertitle,discount,promocode);
                            offerref.push().setValue(offerCode).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    dialogPlus.dismiss();
                                    progressDialog.dismiss();
                                }
                            });
                        }
                    }
                });

            }
        });
        return view;
    }
    public void setofferdata(){

        FirebaseRecyclerOptions<OfferCode> options =
                new FirebaseRecyclerOptions.Builder<OfferCode>()
                        .setQuery(offerref, OfferCode.class)
                        .build();



        adapter=new FirebaseRecyclerAdapter<OfferCode, OfferViewHolder>(options) {


            @NonNull
            @Override
            public OfferViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View offerview = LayoutInflater.from(parent.getContext()).inflate(R.layout.offerrecyclerlayout, parent, false);
                return new OfferViewHolder(offerview);
            }

            @Override
            protected void onBindViewHolder(@NonNull OfferViewHolder holder, int position, @NonNull OfferCode model) {
                holder.offername.setText(model.getOffername());
                holder.promocode.setText(model.getPromocode());
                shimmeroffer.stopShimmer();
                shimmeroffer.setVisibility(View.GONE);
                holder.promocode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ClipboardManager clipboardManager= (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                        clipboardManager.setText(holder.promocode.getText());
                        Toast.makeText(getActivity(), "Copied", Toast.LENGTH_SHORT).show();

                    }
                });holder.deleteofferbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new AlertDialog.Builder(getActivity())
                                .setTitle("Alert!")
                                .setMessage("Are you about to remove offer.Are you sure to remove this offer?")
                                .setPositiveButton(android.R.string.yes, (dialog, which) ->
                                        offerref.child(getRef(holder.getAdapterPosition()).getKey()).removeValue()
                                                .addOnCompleteListener(task -> {
                                                    adapter.notifyDataSetChanged();
                                                        }
                                                ))
                                .setNegativeButton(android.R.string.no, null)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();

                    }
                });

            }
        };
        offerrecycler.setAdapter(adapter);
        adapter.startListening();
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