package com.reemzet.chandan.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.onesignal.OneSignal;
import com.reemzet.chandan.Models.CartModel;
import com.reemzet.chandan.Models.OrderDetails;
import com.reemzet.chandan.Models.UserModel;
import com.reemzet.chandan.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import dev.shreyaspatil.easyupipayment.EasyUpiPayment;
import dev.shreyaspatil.easyupipayment.exception.AppNotFoundException;
import dev.shreyaspatil.easyupipayment.listener.PaymentStatusListener;
import dev.shreyaspatil.easyupipayment.model.PaymentApp;
import dev.shreyaspatil.easyupipayment.model.TransactionDetails;


public class PaymentPage extends Fragment implements PaymentStatusListener {

  TextView phonepebtn,gpaybtn,amazonbtn,paytmbtn,codbtn,totalamount;
    EasyUpiPayment easyUpiPayment;
    Toolbar toolbar;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference usercartref,userref,orderref,admincart,admindeviceid;
    NavController navController;
    UserModel userModel;
    OrderDetails orderDetails;
    ProgressDialog progressDialog;
String totalprice;
    String orderid;
CartModel cartModel;
    UserModel adminuser;
    String admindeviceId;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
     View view= inflater.inflate(R.layout.fragment_payment_page, container, false);
       phonepebtn=view.findViewById(R.id.button_pay);
        gpaybtn=view.findViewById(R.id.gpaybtn);
        amazonbtn=view.findViewById(R.id.amazonpaybtn);
        paytmbtn=view.findViewById(R.id.paytmbtn);
        codbtn=view.findViewById(R.id.codbtn);
        totalamount=view.findViewById(R.id.totalamount);
        database=FirebaseDatabase.getInstance();
        usercartref=database.getReference("App/user");
        userref=database.getReference("App/user");
        orderref=database.getReference("App/user");
        admincart=database.getReference("App/Admincart");
        admindeviceid=database.getReference("App/Admindeviceid");
        mAuth=FirebaseAuth.getInstance();
        NavHostFragment navHostFragment = (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        assert navHostFragment != null;
        navController = navHostFragment.getNavController();
        totalprice=getArguments().getString("totalprice");


        totalamount.setText("for paying "+"\u20B9"+totalprice);

        getuserdetails();
        getAdmindeviceid();
        gpaybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Sorry Only COD", Toast.LENGTH_SHORT).show();
            }
        });
        amazonbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Sorry Only COD", Toast.LENGTH_SHORT).show();
            }
        });
        paytmbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Sorry Only COD", Toast.LENGTH_SHORT).show();
            }
        });

        codbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showloding();
                copycartitemtomyorder("Cod");
            }
        });
       phonepebtn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              Toast.makeText(getActivity(), "Sorry Only COD", Toast.LENGTH_SHORT).show();
          }
        });


        toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Payment ");
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.popBackStack(R.id.home2,true);
                NavHostFragment.findNavController(PaymentPage.this).navigate(R.id.home2);
            }
        });
    return view;
    }

    private void getAdmindeviceid() {
        admindeviceid.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                admindeviceId=snapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void pay(PaymentApp paymentApp){
        EasyUpiPayment.Builder builder = new EasyUpiPayment.Builder(getActivity())
                .with(paymentApp)
                .setPayeeVpa("9525581574@paytm")
                .setPayeeName("Ravi ranjan kumar")
                .setPayeeMerchantCode("mJoqEh27922510661506")
                .setTransactionId("T2020090212345")
                .setTransactionRefId("T2020090212345")
                .setDescription("Description")
                .setAmount("1.00");

        try {
            easyUpiPayment = builder.build();
        } catch (AppNotFoundException e) {
            e.printStackTrace();
        }

        easyUpiPayment.startPayment();
    }

    @Override
    public void onTransactionCancelled() {
        toast("Cancelled by user");
        Toast.makeText(getActivity(),"camnel",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTransactionCompleted(@NonNull TransactionDetails transactionDetails) {


        switch (transactionDetails.getTransactionStatus()) {
            case SUCCESS:
                onTransactionSuccess();
                break;
            case FAILURE:
                onTransactionFailed();
                break;
            case SUBMITTED:
                onTransactionSubmitted();
                break;
        }
    }

    private void onTransactionSuccess() {
        // Payment Success
        toast("Success");

    }

    private void onTransactionSubmitted() {
        // Payment Pending
        toast("Pending | Submitted");

    }

    private void onTransactionFailed() {
        // Payment Failed
        toast("Failed");

    }

    private void toast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    public void getuserdetails(){
        userref.child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

              userModel=snapshot.getValue(UserModel.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void copycartitemtomyorder(String paymentoption){
        Date cd = Calendar.getInstance().getTime();
        Calendar c=Calendar.getInstance();
        SimpleDateFormat format=new SimpleDateFormat("h:mm:ss a");
        String time=format.format(c.getTime());
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String formattedDate = df.format(cd);
        orderid=orderref.child(mAuth.getUid()).child("order").push().getKey();
        orderDetails=new OrderDetails(formattedDate,paymentoption,time,"Orders","4",totalprice,orderid,"Pending",userModel.getUseraddress(),userModel.getUserlocation(),userModel.getUsername().toUpperCase(Locale.ROOT),userModel.getUserphone(),userModel.getUseruid(),userModel.getDeviceid());

        admincart.child(orderid).setValue(orderDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                orderref.child(mAuth.getUid()).child("order").child(orderid).setValue(orderDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        usercartref.child(mAuth.getUid()).child("usercart").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot items : snapshot.getChildren()){
                                    cartModel=items.getValue(CartModel.class);
                                    uploadorder();

                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
                    }
                });
            }
        });



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

    public void uploadorder(){
        orderref.child(mAuth.getUid()).child("order").child(orderid).push().setValue(cartModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
            }
        });
        admincart.child(orderid).push().setValue(cartModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                usercartref.child(mAuth.getUid()).child("usercart").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        try {
                            OneSignal.postNotification(new JSONObject("{'contents': {'en': '"+ "Your order has been successfully placed.now you can check status in my order section "+"'}, 'include_player_ids': ['" + OneSignal.getDeviceState().getUserId() + "']}"),
                                    new OneSignal.PostNotificationResponseHandler() {
                                        @Override
                                        public void onSuccess(JSONObject response) {

                                        }

                                        @Override
                                        public void onFailure(JSONObject response) {

                                        }
                                    });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            OneSignal.postNotification(new JSONObject("{'contents': {'en': '"+ "Your have new order.you can check it in  order section "+"'}, 'include_player_ids': ['" + admindeviceId + "']}"),
                                    new OneSignal.PostNotificationResponseHandler() {
                                        @Override
                                        public void onSuccess(JSONObject response) {

                                        }

                                        @Override
                                        public void onFailure(JSONObject response) {

                                        }
                                    });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                        navController.popBackStack(R.id.cartFragment,true);
                        navController.navigate(R.id.orderConfirmPage);
                    }
                });
            }
        });
    }

    }