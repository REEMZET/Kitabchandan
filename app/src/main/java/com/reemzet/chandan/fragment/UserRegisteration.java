package com.reemzet.chandan.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.onesignal.OneSignal;
import com.reemzet.chandan.Models.UserModel;
import com.reemzet.chandan.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class UserRegisteration extends Fragment {

EditText etname,etcity,etpincode,etaddress;
String username,usercity,userpincode,useraddress;
TextView submit;
ProgressDialog progressDialog;
boolean fieldboolean;
FirebaseAuth mAuth;
FirebaseDatabase database;
DatabaseReference userref;
    NavController navController;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_user_registeration, container, false);

        etname=view.findViewById(R.id.etname);
        etaddress=view.findViewById(R.id.etaddress);
        etcity=view.findViewById(R.id.etcity);
        etpincode=view.findViewById(R.id.etpincode);
        submit=view.findViewById(R.id.submit);
        mAuth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        userref=database.getReference("App/user");
        NavHostFragment navHostFragment = (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        assert navHostFragment != null;
        navController = navHostFragment.getNavController();

        String date=new SimpleDateFormat("d/M/yy", Locale.getDefault()).format(new Date());
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showloding();
               if (!checkandsetdata()){
                   progressDialog.dismiss();
                   Toast.makeText(getActivity(),"please enter all information",Toast.LENGTH_SHORT).show();
               }else {
                   UserModel user=new UserModel(username, mAuth.getUid(),usercity,"null",date,mAuth.getCurrentUser().getPhoneNumber(),useraddress,userpincode,"Normal", OneSignal.getDeviceState().getUserId());
                   userref.child(mAuth.getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                       @Override
                       public void onComplete(@NonNull Task<Void> task) {
                           progressDialog.dismiss();
                          navController.popBackStack();
                           NavHostFragment.findNavController(UserRegisteration.this).navigate(R.id.menu);
                       }
                   }).addOnFailureListener(new OnFailureListener() {
                       @Override
                       public void onFailure(@NonNull Exception e) {
                           progressDialog.dismiss();
                           Toast.makeText(getActivity(),e.getMessage().toString(),Toast.LENGTH_SHORT).show();
                       }
                   });
               }
            }
        });






        return view;
    }

    private boolean checkandsetdata() {
        if (etname.getText().toString().isEmpty()){
            etname.setError("empty");
            etname.requestFocus();
            fieldboolean=false;
        }else if (etcity.getText().toString().isEmpty()){
            etcity.setError("empty");
            etcity.requestFocus();
            fieldboolean=false;
        }else if (etpincode.getText().toString().isEmpty()){
            etpincode.setError("empty");
            etpincode.requestFocus();
            fieldboolean=false;
        }else if (etaddress.getText().toString().isEmpty()){
            etaddress.setError("empty");
            etaddress.requestFocus();
            fieldboolean=false;
        }else {
            username=etname.getText().toString();
            usercity=etcity.getText().toString();
            userpincode=etpincode.getText().toString();
            useraddress=etaddress.getText().toString();
            fieldboolean=true;
        }
        return fieldboolean;
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
}