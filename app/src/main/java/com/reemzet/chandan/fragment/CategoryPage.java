package com.reemzet.chandan.fragment;

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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.reemzet.chandan.Adapter.BookViewHolder;
import com.reemzet.chandan.Models.ItemDetails;
import com.reemzet.chandan.R;

import java.util.ArrayList;

public class CategoryPage extends Fragment {
    RecyclerView categoryrecycelerview;
    String category;
    FirebaseDatabase database;
    DatabaseReference categoryref;
    Query query;
    FirebaseRecyclerAdapter<ItemDetails,BookViewHolder>adapter;
    NavController navController;
    Toolbar toolbar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_category_page, container, false);
        categoryrecycelerview=view.findViewById(R.id.categoryrecycler);

        category=getArguments().getString("category");
        database=FirebaseDatabase.getInstance();
        categoryref=database.getReference("App/Items/itemlist");
        query=categoryref.orderByChild("itemcat").startAt(category.toString()).endAt(category+ "\uf8ff");

        NavHostFragment navHostFragment = (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        assert navHostFragment != null;
        navController = navHostFragment.getNavController();
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        categoryrecycelerview.setLayoutManager(staggeredGridLayoutManager);

        setcategory();
        toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(category);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    navController.popBackStack(R.id.home2,true);
                    NavHostFragment.findNavController(CategoryPage.this).navigate(R.id.home2);
            }
        });
            return view;

    }

    public void setcategory(){

        FirebaseRecyclerOptions<ItemDetails> options =
                new FirebaseRecyclerOptions.Builder<ItemDetails>()
                        .setQuery(query, ItemDetails.class)
                        .build();
                    checkitemcount();

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

                 navController.navigate(R.id.action_categoryPage_to_bookDescPage,bundle);
                });
                /*shimmerecent.stopShimmer();
                shimmerecent.setVisibility(View.GONE);
                recentrecycler.setVisibility(View.VISIBLE);*/
            }

            @NonNull
            @Override
            public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View bookview = LayoutInflater.from(parent.getContext()).inflate(R.layout.searchbooklayout, parent, false);
                return new BookViewHolder(bookview);
            }
        };
       categoryrecycelerview.setAdapter(adapter);
        adapter.startListening();
    }

    private void checkitemcount() {
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()){
                    query=categoryref.orderByChild("itemexam").startAt(category.toString()).endAt(category+ "\uf8ff");
                    setcategory();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}