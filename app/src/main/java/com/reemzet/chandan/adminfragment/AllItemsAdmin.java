package com.reemzet.chandan.adminfragment;

import android.graphics.Paint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.reemzet.chandan.Adapter.BookViewHolder;
import com.reemzet.chandan.Models.ItemDetails;
import com.reemzet.chandan.R;

import java.util.ArrayList;

public class AllItemsAdmin extends Fragment {

    RecyclerView allitemrecyclerview;
    FirebaseDatabase database;
    DatabaseReference allitemsref;
    Query query;
    FirebaseRecyclerAdapter<ItemDetails,BookViewHolder>adapter;
    NavController navController;
    Toolbar toolbar;
    AutoCompleteTextView etsearchitem;
    ArrayList<String> itemlist;
    ItemDetails allitemmodel;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view=inflater.inflate(R.layout.fragment_all_items_admin, container, false);
        allitemrecyclerview=view.findViewById(R.id.allitemrecycler);
        etsearchitem=view.findViewById(R.id.etitemsearch);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL);
        allitemrecyclerview.setLayoutManager(staggeredGridLayoutManager);
        NavHostFragment navHostFragment = (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        assert navHostFragment != null;
        navController = navHostFragment.getNavController();
        database=FirebaseDatabase.getInstance();
        allitemsref=database.getReference("App/Items/itemlist");
        query=allitemsref;
        itemlist=new ArrayList<>();
        setAllItem();
        toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("All items");
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.popBackStack(R.id.adminHome, true);
                NavHostFragment.findNavController(AllItemsAdmin.this).navigate(R.id.adminHome);
            }
        });
        etsearchitem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                    if (editable.length()==0){
                      query=  allitemsref=database.getReference("App/Items/itemlist");
                      setAllItem();
                    }else {
                        query=allitemsref.orderByChild("itemtitle").startAt(editable.toString()).endAt(editable.toString()+ "\uf8ff");
                        setAllItem();
                    }



            }
        });





      return view;
    }

    private void setAllItem() {
        FirebaseRecyclerOptions<ItemDetails> options =
                new FirebaseRecyclerOptions.Builder<ItemDetails>()
                        .setQuery(query, ItemDetails.class)
                        .build();

        adapter=new FirebaseRecyclerAdapter<ItemDetails, BookViewHolder>(options) {
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
                    Bundle bundle=new Bundle();
                    bundle.putString("itemimg", model.getItemimg());
                    bundle.putString("itemtitle",model.getItemtitle());
                    bundle.putString("itemprice",model.getItemprice());
                    bundle.putString("itemmrp",model.getItemmrp());
                    bundle.putString("itemdesc",model.getItemdesc());
                    bundle.putString("itemstatus",model.getItemstatus());
                    bundle.putString("itemcat",model.getItemcat());
                    bundle.putString("itemavail",model.getItemavail());
                    bundle.putString("itemstatus",model.getItemstatus());
                    bundle.putString("itemexam",model.getItemexam());
                    bundle.putString("itemid",model.getItemid());

          navController.navigate(R.id.action_allItemsAdmin_to_itemdescpage,bundle);
                });




            }

            @NonNull
            @Override
            public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View bookview = LayoutInflater.from(parent.getContext()).inflate(R.layout.bookitemlayout, parent, false);
                return new BookViewHolder(bookview);
            }
        };

       allitemrecyclerview.setAdapter(adapter);
        adapter.startListening();
    }
}