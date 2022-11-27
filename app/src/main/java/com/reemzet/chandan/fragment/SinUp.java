package com.reemzet.chandan.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.reemzet.chandan.Adapter.SliderSinupAdapter;
import com.reemzet.chandan.Models.ViewpagerModel;
import com.reemzet.chandan.R;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


public class SinUp extends Fragment {


    EditText phonenumber, etotp;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    NavController navController;
    TextView submitbutton;
    ViewPager2 viewPager2;
    ProgressDialog progressDialog;
    ConstraintLayout otpconstraint;
    String otpid;
    private final Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
        }
    };
    ShimmerFrameLayout shimmerFrameLayout;
    Handler slideHandler = new Handler();
    ArrayList<ViewpagerModel> viewpagerModelArrayList;
    DatabaseReference slider,userref;
    String phoneno;
    FirebaseAuth mAuth;
    Boolean codesent=false;
    boolean exit;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sin_up, container, false);
        shimmerFrameLayout = view.findViewById(R.id.sinupshimmer);
        viewPager2 = view.findViewById(R.id.signupsliderviewpager);
        NavHostFragment navHostFragment = (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        assert navHostFragment != null;
        navController = navHostFragment.getNavController();
        phonenumber = view.findViewById(R.id.etphone);
        mAuth=FirebaseAuth.getInstance();
        etotp = view.findViewById(R.id.etotp);
        otpconstraint=view.findViewById(R.id.otpconstraint);
        progressDialog = new ProgressDialog(getActivity());
        if (progressDialog.isShowing()){
            progressDialog.dismiss();
        }
        submitbutton = view.findViewById(R.id.submitbtn);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database = FirebaseDatabase.getInstance();
        slider = database.getReference("App/sinupslider");
        userref=database.getReference("App/user");

        submitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (codesent){
                    if (etotp.getText().toString().length()<6){
                        etotp.setError("Invalid Otp");
                    }else {
                        showloding();
                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(otpid, etotp.getText().toString());
                        signInWithPhoneAuthCredential(credential);
                    }
                }else {
                    if (phonenumber.getText().toString().length()<10){
                        phonenumber.setError("invalid phone");
                    }else {
                        phoneno=phonenumber.getText().toString();
                        otpsent(phoneno);
                        showloding();
                    }
                }
            }
        });

        slidermethod();



        return view;
    }

    public void slidermethod() {
        viewpagerModelArrayList = new ArrayList<>();
        slider.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                viewpagerModelArrayList.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    ViewpagerModel postermodel = snapshot1.getValue(ViewpagerModel.class);
                    viewpagerModelArrayList.add(postermodel);
                }
                viewPager2.setAdapter(new SliderSinupAdapter(getContext(), viewpagerModelArrayList, viewPager2));
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
                viewPager2.setVisibility(View.VISIBLE);
                viewPager2.setClipToPadding(false);
                viewPager2.setClipChildren(false);
                viewPager2.setOffscreenPageLimit(3);
                viewPager2.setCurrentItem(3, true);
                viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
                CompositePageTransformer transformer = new CompositePageTransformer();
                transformer.addTransformer(new ViewPager2.PageTransformer() {
                    @Override
                    public void transformPage(@NonNull View page, float position) {
                        float a = 1 - Math.abs(position);
                        page.setScaleY(0.85f + a * 0.15f);
                    }
                });
                viewPager2.setPageTransformer(transformer);
                viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                    @Override
                    public void onPageSelected(int position) {
                        super.onPageSelected(position);
                        slideHandler.removeCallbacks(sliderRunnable);
                        slideHandler.postDelayed(sliderRunnable, 4000);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    public void otpsent(String phone) {
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(getActivity(), "failed" + e.getMessage(), Toast.LENGTH_LONG).show();

                // Show a message and update the UI
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                otpid=verificationId;
                progressDialog.dismiss();
                otpconstraint.setVisibility(View.VISIBLE);
                phonenumber.setEnabled(false);
                progressDialog.dismiss();
                etotp.requestFocus();
                codesent=true;
                Toast.makeText(getActivity(),"Please enter Otp",Toast.LENGTH_SHORT).show();
            }
        };
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+91"+phone)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(getActivity())                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), task -> {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                         checkexiting();
                    } else {
                        progressDialog.dismiss();
                        // Sign in failed, display a message and update the UI
                        Toast.makeText(getActivity(), "failed to verify", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void checkexiting() {
        showloding();
        userref.child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    progressDialog.dismiss();
                    navController.popBackStack();
                    NavHostFragment.findNavController(SinUp.this).navigate(R.id.menu);
                }else {
                    progressDialog.dismiss();
                    navController.popBackStack();
                    NavHostFragment.findNavController(SinUp.this).navigate(R.id.userRegisteration);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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

    @Override
    public void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser()!=null) {
            checkexiting();
        }
    }
}


