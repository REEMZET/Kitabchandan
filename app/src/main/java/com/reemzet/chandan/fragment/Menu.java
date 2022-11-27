package com.reemzet.chandan.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.reemzet.chandan.R;


public class Menu extends Fragment {
    CardView bookstorecard,rubiks;
    NavController navController;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_menu, container, false);
        rubiks=view.findViewById(R.id.rubiks);
        bookstorecard=view.findViewById(R.id.bookstorecard);
        progressDialog = new ProgressDialog(getActivity());

        NavHostFragment navHostFragment = (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        assert navHostFragment != null;
        navController = navHostFragment.getNavController();
        bookstorecard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(Menu.this).navigate(R.id.home2);
            }
        });
        rubiks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        return view;

    }

    @Override
    public void onStart() {
        super.onStart();

    }
}