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

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.reemzet.chandan.Adapter.BookViewHolder;
import com.reemzet.chandan.Models.ItemDetails;
import com.reemzet.chandan.R;


public class SearchItem extends Fragment {

        RecyclerView searchrecyclerview;
    FirestoreRecyclerAdapter<ItemDetails, BookViewHolder> adapter;
    EditText searchet;
    Query query;
    NavController navController;
    Toolbar toolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_search_item, container, false);

        searchrecyclerview=view.findViewById(R.id.searchrecyclerview);
        searchet=view.findViewById(R.id.searchet);
        NavHostFragment navHostFragment = (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        assert navHostFragment != null;
        navController = navHostFragment.getNavController();
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        searchrecyclerview.setLayoutManager(staggeredGridLayoutManager);

        query= FirebaseFirestore.getInstance()
                .collection("Items")
                .limit(30);
        setSearchitem();
        searchet.requestFocus();
        searchet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length()==0){
                    query= FirebaseFirestore.getInstance()
                            .collection("Items")
                            .limit(30);
                    setSearchitem();
                }else {
                    query= FirebaseFirestore.getInstance()
                            .collection("Items").whereArrayContains("itemtag",editable.toString())
                            .limit(10);
                    setSearchitem();
                }


            }
        });
        toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Search");
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.popBackStack(R.id.home2,true);
                NavHostFragment.findNavController(SearchItem.this).navigate(R.id.home2);
            }
        });


    return view;
    }
    public void setSearchitem(){
        FirestoreRecyclerOptions<ItemDetails> options = new FirestoreRecyclerOptions.Builder<ItemDetails>()
                .setQuery(query, ItemDetails.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<ItemDetails, BookViewHolder>(options) {

            @NonNull
            @Override
            public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.searchbooklayout, parent, false);
                return new BookViewHolder(view);
            }

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
                    navController.navigate(R.id.action_searchItem_to_bookDescPage,bundle);
                });

            }

        };
        searchrecyclerview.setAdapter(adapter);
        adapter.startListening();

    }

}


















