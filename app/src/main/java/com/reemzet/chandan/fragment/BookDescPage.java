package com.reemzet.chandan.fragment;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.Paint;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.reemzet.chandan.Adapter.BookViewHolder;
import com.reemzet.chandan.Models.CartModel;
import com.reemzet.chandan.Models.ItemDetails;
import com.reemzet.chandan.R;


public class BookDescPage extends Fragment {

        ImageView bookimageview;
        String bookimg,itemprice,itemmrp,itemdesc,itemtitle,itemcat,itemavail,itemid;
        TextView tvitemmrp,tvitemprice,tvitemtitle,tvitemdesc,itemavailability;
        FirebaseDatabase database;
        DatabaseReference similaritemref,usercartref;
        RecyclerView similaritemrecyceler;
    FirebaseRecyclerAdapter<ItemDetails, BookViewHolder> similaritemlistadapter;
    NavController navController;
    EditText qtyedittext;
    FirebaseAuth mAuth;
    LinearLayout addtocartlayout;
    ProgressDialog progressDialog;
    Toolbar toolbar;
    TextView textCartItemCount;
    int mCartItemCount = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view= inflater.inflate(R.layout.fragment_book_desc_page, container, false);

        bookimageview=view.findViewById(R.id.bookimg);
        tvitemtitle=view.findViewById(R.id.itemname);
        tvitemmrp=view.findViewById(R.id.itemmrp);
        tvitemprice=view.findViewById(R.id.itemprice);
        tvitemdesc=view.findViewById(R.id.itemdesc);
        qtyedittext=view.findViewById(R.id.qteditext);
        addtocartlayout=view.findViewById(R.id.addtocartlayout);
        itemavailability=view.findViewById(R.id.itemavailability);
        similaritemrecyceler=view.findViewById(R.id.similarrecyclerview);
        similaritemrecyceler.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        NavHostFragment navHostFragment = (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        assert navHostFragment != null;
        navController = navHostFragment.getNavController();
        mAuth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        similaritemref=database.getReference("App").child("Items/itemlist");
        usercartref=database.getReference("App/user").child(mAuth.getUid()).child("usercart");
        toolbar = getActivity().findViewById(R.id.toolbar);
        textCartItemCount=getActivity().findViewById(R.id.tvcartcount);

        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.popBackStack(R.id.home2,true);
                NavHostFragment.findNavController(BookDescPage.this).navigate(R.id.home2);
            }
        });
        getStringfrombundle();
        setDatatoview();
        setupBadge();

        addtocartlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showloding();
                if (qtyedittext.getText().toString().isEmpty()|| Integer.parseInt(qtyedittext.getText().toString())<1){
                        qtyedittext.setError("item can't be less than zero");
                        qtyedittext.requestFocus();
                        progressDialog.dismiss();
                }else {
                    CartModel cartModel=new CartModel(itemid,qtyedittext.getText().toString(),itemtitle,itemmrp,itemprice,bookimg);
                    usercartref.push().setValue(cartModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Added to cart", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });

        setSimilaritema();

       return view;
    }
    public void getStringfrombundle(){
        bookimg=getArguments().getString("itemimg");
        itemprice=getArguments().getString("itemprice");
        itemdesc=getArguments().getString("itemdesc");
        itemmrp=getArguments().getString("itemmrp");
        itemtitle=getArguments().getString("itemtitle");
        itemcat=getArguments().getString("itemcat");
        itemavail=getArguments().getString("itemavail");
        itemid=getArguments().getString("itemid");
        toolbar.setTitle(itemtitle);
    }
    public void setDatatoview(){
        Glide.with(getActivity())
                .load(bookimg)
                .centerCrop()
                .placeholder(R.drawable.books)
                .into(bookimageview);

        tvitemtitle.setText(itemtitle);
        tvitemprice.setText("\u20B9"+itemprice);
        tvitemmrp.setText("Mrp:-"+itemmrp);
        tvitemmrp.setPaintFlags(tvitemmrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        tvitemdesc.setText(itemdesc);
        itemavailability.setText(itemavail);
        if (itemavailability.getText().equals("Out of Stock")){
            itemavailability.setTextColor(Color.RED);
        }
    }

    public void setSimilaritema(){
      Query query = similaritemref.orderByChild("itemcat").startAt(itemcat).endAt(itemcat+ "\uf8ff").limitToFirst(5);
        FirebaseRecyclerOptions<ItemDetails> options =
                new FirebaseRecyclerOptions.Builder<ItemDetails>()
                        .setQuery(query, ItemDetails.class)
                        .build();

    similaritemlistadapter =new FirebaseRecyclerAdapter<ItemDetails, BookViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull BookViewHolder holder, int position, @NonNull ItemDetails model) {
                holder.bookstatus.setText(model.getItemstatus());
                holder.bookmrp.setText("Mrp:-"+model.getItemmrp());
                holder.bookmrp.setPaintFlags(holder.bookmrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                holder.booktile.setText(model.getItemtitle());
                holder.bookprice.setText("\u20B9"+model.getItemprice());

                Glide.with(getActivity())
                        .load(model.getItemimg())
                        .centerCrop()
                        .placeholder(R.drawable.books)
                        .into(holder.bookimg);
                holder.itemconstraint.setOnClickListener(view -> {
                    Glide.with(getActivity())
                            .load(model.getItemimg())
                            .centerCrop()
                            .placeholder(R.drawable.books)
                            .into(bookimageview);

                    tvitemtitle.setText(model.getItemtitle());
                    tvitemprice.setText("\u20B9"+model.getItemprice());
                    tvitemmrp.setText("Mrp:-"+model.getItemprice());
                    tvitemmrp.setPaintFlags(tvitemmrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    tvitemdesc.setText(model.getItemdesc());
                    itemavailability.setText(itemavail);
                    bookimg=model.getItemimg();
                    itemprice=model.getItemprice();
                    itemdesc=model.getItemdesc();
                    itemmrp=model.getItemmrp();
                    itemtitle=model.getItemtitle();
                    itemcat= model.getItemcat();
                    itemavail=model.getItemavail();
                    itemid=model.getItemid();
                    if (itemavailability.getText().equals("Out of Stock")){
                        itemavailability.setTextColor(Color.RED);
                    }

                    setSimilaritema();
                });
            }

            @NonNull
            @Override
            public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View bookview = LayoutInflater.from(parent.getContext()).inflate(R.layout.bookitemlayout, parent, false);
                return new BookViewHolder(bookview);
            }
        };
        similaritemrecyceler.setAdapter(similaritemlistadapter);
        similaritemlistadapter.startListening();
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

    private void setupBadge() {

        if (mAuth.getCurrentUser()!=null) {
            usercartref.child(mAuth.getUid()).child("usercart").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        mCartItemCount=Integer.valueOf((int) snapshot.getChildrenCount());
                        if (textCartItemCount != null) {
                            if (mCartItemCount == 0) {
                                if (textCartItemCount.getVisibility() != View.GONE) {
                                    textCartItemCount.setVisibility(View.GONE);
                                }
                            } else {
                                textCartItemCount.setText(String.valueOf(Math.min(mCartItemCount, 99)));
                                if (textCartItemCount.getVisibility() != View.VISIBLE) {
                                    textCartItemCount.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }



    }
}