package com.reemzet.chandan.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.onesignal.OneSignal;
import com.reemzet.chandan.Models.UserModel;
import com.reemzet.chandan.R;


public class SplashScreen extends Fragment {

    FirebaseAuth mAuth;
    DatabaseReference userref;
    FirebaseDatabase database;
    NavController navController;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_splash_screen, container, false);
        mAuth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        userref=database.getReference("App/user");
        NavHostFragment navHostFragment = (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        assert navHostFragment != null;
        navController = navHostFragment.getNavController();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mAuth.getCurrentUser()!=null) {
                    checkexiting();
                }else {
                    navController.popBackStack();
                    NavHostFragment.findNavController(SplashScreen.this).navigate(R.id.sinUpfragment);
                }
            }
        }, 3000);


   return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mAuth.getCurrentUser()!=null) {
                    checkexiting();
                }else {
                  // navController.popBackStack();
                  //NavHostFragment.findNavController(SplashScreen.this).navigate(R.id.sinUpfragment);
                   // navController.navigate(R.id.action_splashScreen_to_sinUp);


                }
            }
        }, 3000);
    }

    private void checkexiting() {

        userref.child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    UserModel userModel=snapshot.getValue(UserModel.class);
                    if (userModel.getAccounttype().equals("Admin")){
                        navController.popBackStack();
                        NavHostFragment.findNavController(SplashScreen.this).navigate(R.id.adminHome);
                        DatabaseReference admindeviceid=database.getReference("App/Admindeviceid");
                        admindeviceid.setValue(OneSignal.getDeviceState().getUserId());

                    }else {
                        navController.popBackStack();
                        NavHostFragment.findNavController(SplashScreen.this).navigate(R.id.menu);
                    }
                }else {
                    navController.popBackStack();
                    NavHostFragment.findNavController(SplashScreen.this).navigate(R.id.userRegisteration);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}