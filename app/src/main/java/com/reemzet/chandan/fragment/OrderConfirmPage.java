package com.reemzet.chandan.fragment;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.reemzet.chandan.R;

public class OrderConfirmPage extends Fragment {

NavController navController;
Toolbar toolbar;
TextView tvgotomyorder;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_order_confirm_page, container, false);
        NavHostFragment navHostFragment = (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        assert navHostFragment != null;
        navController = navHostFragment.getNavController();
        tvgotomyorder=view.findViewById(R.id.tvgotomyorder);

        toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Order Confirm");
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.popBackStack(R.id.home2,true);
                NavHostFragment.findNavController(OrderConfirmPage.this).navigate(R.id.home2);
            }
        });
        tvgotomyorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.popBackStack(R.id.home2,true);
                navController.navigate(R.id.myOrders);

            }
        });

return view;
    }
}