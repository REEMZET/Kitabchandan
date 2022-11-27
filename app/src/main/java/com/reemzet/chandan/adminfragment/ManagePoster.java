package com.reemzet.chandan.adminfragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.reemzet.chandan.Adapter.PosterViewHolder;
import com.reemzet.chandan.Models.ViewpagerModel;
import com.reemzet.chandan.R;

public class ManagePoster extends Fragment {

    DatabaseReference posterref;
    RecyclerView posterrecycler;
    Toolbar toolbar;
    FirebaseRecyclerAdapter<ViewpagerModel, PosterViewHolder> adapter;
    FloatingActionButton addposterbtn;
    String gdrivecode="https://drive.google.com/uc?export=download&id=";
    String exculdecode1="https://drive.google.com/file/d/";
    String exculdecode2="/view?usp=sharing";
    String exculdecode3="/view?usp=drivesdk";
    String imageurl;

    ProgressDialog progressDialog;
    NavController navController;
    ImageView imgok;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_manage_poster2, container, false);


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database=FirebaseDatabase.getInstance();
        posterref=database.getReference("App").child("Slider");

        posterrecycler=view.findViewById(R.id.posterrecycler);

        addposterbtn=view.findViewById(R.id.addposterbtn);
        NavHostFragment navHostFragment = (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        assert navHostFragment != null;
        navController = navHostFragment.getNavController();

        LinearLayoutManager offerlinearLayoutManager = new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false);

        posterrecycler.setLayoutManager(offerlinearLayoutManager);
        setposterdata();
        toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Manage Poster");
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.popBackStack(R.id.adminHome,true);
                NavHostFragment.findNavController(ManagePoster.this).navigate(R.id.adminHome);
            }
        });

        addposterbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogPlus dialogPlus = DialogPlus.newDialog(getActivity())
                        .setContentHolder(new ViewHolder(R.layout.addposterlayout))
                        .setGravity(Gravity.CENTER)
                        .setCancelable(false)
                        .create();
                dialogPlus.show();
                View dialogview = dialogPlus.getHolderView();
                EditText etposterurl=dialogview.findViewById(R.id.etposterurl);
                Button postbtn=dialogview.findViewById(R.id.postbtn);
                ImageView closewindown=dialogview.findViewById(R.id.closewindowbtn);
                imgok=dialogview.findViewById(R.id.imgokbtn2);
                ImageView imageView=dialogview.findViewById(R.id.posterimgview);
                etposterurl.requestFocus();
                imgok.setOnClickListener(view12 -> {
                    if (!etposterurl.getText().toString().isEmpty()){
                        String imgurl1=etposterurl.getText().toString().replace(exculdecode1,gdrivecode);
                        String imgurl2=imgurl1.replace(exculdecode2,"");
                        String finalimgurl=imgurl2.replace(exculdecode3,"");
                        etposterurl.setText(finalimgurl);
                        imageurl=finalimgurl;

                        Glide.with(getActivity())
                                .load(finalimgurl)
                                .centerCrop()
                                .placeholder(R.drawable.picture)
                                .into(imageView);

                    }
                });
                postbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!etposterurl.getText().toString().isEmpty()){
                            String imgurl1=etposterurl.getText().toString().replace(exculdecode1,gdrivecode);
                            String imgurl2=imgurl1.replace(exculdecode2,"");
                            String finalimgurl=imgurl2.replace(exculdecode3,"");
                            etposterurl.setText(finalimgurl);
                            imageurl=finalimgurl;
                            ViewpagerModel model=new ViewpagerModel(imageurl,"no");
                            posterref.push().setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                dialogPlus.dismiss();
                                }
                            });
                        }else {
                            etposterurl.setError("invalid url");
                            etposterurl.requestFocus();
                        }

                    }
                });
                closewindown.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogPlus.dismiss();
                    }
                });
            }
        });

        return view;
    }
    private void setposterdata() {

        FirebaseRecyclerOptions<ViewpagerModel> options =
                new FirebaseRecyclerOptions.Builder<ViewpagerModel>()
                        .setQuery(posterref, ViewpagerModel.class)
                        .build();



        adapter=new FirebaseRecyclerAdapter<ViewpagerModel, PosterViewHolder>(options) {


            @NonNull
            @Override
            public PosterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View offerview = LayoutInflater.from(parent.getContext()).inflate(R.layout.posterlayout, parent, false);
                return new PosterViewHolder(offerview);
            }

            @Override
            protected void onBindViewHolder(@NonNull PosterViewHolder holder, int position, @NonNull ViewpagerModel model) {

                Glide.with(getActivity())
                        .load(model.getPosterurl())
                        .centerCrop()
                        .placeholder(R.drawable.picture)
                        .into(holder.posterimg);
                holder.deleteposterbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new AlertDialog.Builder(getActivity())
                                .setTitle("Alert!")
                                .setMessage("Are you about to remove poster.Are you sure to remove this poster?")
                                .setPositiveButton(android.R.string.yes, (dialog, which) ->
                                        posterref.child(getRef(holder.getAdapterPosition()).getKey()).removeValue()
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
        posterrecycler.setAdapter(adapter);
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