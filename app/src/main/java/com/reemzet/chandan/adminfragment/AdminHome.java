package com.reemzet.chandan.adminfragment;

import android.app.UiAutomation;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.L;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.onesignal.OneSignal;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.reemzet.chandan.Adapter.AdminCatListAdapter;
import com.reemzet.chandan.Adapter.AdminOrderViewHOLDER;
import com.reemzet.chandan.Adapter.MyorderViewholder;
import com.reemzet.chandan.Models.OrderDetails;
import com.reemzet.chandan.R;
import com.reemzet.chandan.notification.SendNotification;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class AdminHome extends Fragment {

    
    TextView tvdate,addnewitem;
    NavController navController;
    RecyclerView todaysorder;
    FirebaseDatabase database;
    DatabaseReference adminorder,allitemsref,allcat;
    Query query;
    BitmapDrawable bitmapDrawable;
    Bitmap bitmap;
    String formattedDate;
    FirebaseRecyclerAdapter<OrderDetails, AdminOrderViewHOLDER> adapter;
    ConstraintLayout constraintallorder,constraintallitems,constraintitemcat,constraintoffer,constraintnotification,constraintposter,constraintstoredetails;
    TextView tvnoorder,tvnoofallitems,tvnoofcat;
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    ImageView imageshareviewadmin;
    LinearLayout linearinvite;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view= inflater.inflate(R.layout.fragment_admin_home, container, false);
        tvdate=view.findViewById(R.id.todaydate);
        addnewitem=view.findViewById(R.id.addnewitem);
        NavHostFragment navHostFragment = (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        assert navHostFragment != null;
        navController = navHostFragment.getNavController();
       todaysorder=view.findViewById(R.id.todaysorderrecyclerview);
        drawerLayout=getActivity().findViewById(R.id.drawer);
       constraintallorder=view.findViewById(R.id.constraintallorder);
       constraintallitems=view.findViewById(R.id.constraintallitems);
       constraintoffer=view.findViewById(R.id.constraintoffer);
       constraintstoredetails=view.findViewById(R.id.constraintstoredetails);
       constraintitemcat=view.findViewById(R.id.constitemcat);
       constraintnotification=view.findViewById(R.id.constraintnotification);
       constraintposter=view.findViewById(R.id.constraintposter);
        tvnoorder=view.findViewById(R.id.tvnooorder);
        tvnoofallitems=view.findViewById(R.id.tvnoofallitem);
        tvnoofcat=view.findViewById(R.id.tvnoofcat);
        imageshareviewadmin=view.findViewById(R.id.imageshareviewadminhome);
        linearinvite=view.findViewById(R.id.linearinvite);
        todaysorder.setLayoutManager(new LinearLayoutManager(getActivity()));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        todaysorder.setLayoutManager(linearLayoutManager);
        database= FirebaseDatabase.getInstance();
        adminorder=database.getReference("App/Admincart");
        allitemsref=database.getReference("App/Items/itemlist");
        allcat=database.getReference("App/Items");


        toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_menu_24);
        toolbar.setTitle("Admin Chandan");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                drawerLayout.open();

            }
        });



        addnewitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putString("editoradd","add");
                NavHostFragment.findNavController(AdminHome.this).navigate(R.id.action_adminHome_to_addItems,bundle);
            }
        });
        constraintallorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(AdminHome.this).navigate(R.id.action_adminHome_to_allOrderItemsAdmin);
            }
        });
        constraintallitems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(AdminHome.this).navigate(R.id.action_adminHome_to_allItemsAdmin);
            }
        });
        constraintitemcat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(AdminHome.this).navigate(R.id.action_adminHome_to_adminCatList);
            }
        });
        constraintoffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(AdminHome.this).navigate(R.id.action_adminHome_to_manageOffer);
            }
        });
        constraintposter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(AdminHome.this).navigate(R.id.action_adminHome_to_managePoster2);
            }
        });
        linearinvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                invite();
            }
        });
        constraintstoredetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(AdminHome.this).navigate(R.id.action_adminHome_to_storeDetails);
            }
        });
        constraintnotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*DialogPlus dialogPlus = DialogPlus.newDialog(getActivity())
                        .setContentHolder(new ViewHolder(R.layout.notificationview))
                        .setGravity(Gravity.CENTER)
                        .setCancelable(true)
                        .create();
                dialogPlus.show();
                View dialogview = dialogPlus.getHolderView();
                EditText etnotificationmsg=dialogview.findViewById(R.id.etnotificationmsg);
                Button sendbtn=dialogview.findViewById(R.id.sendnotificationbtn);
                sendbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!etnotificationmsg.getText().toString().isEmpty()){
                      try {
                          Toast.makeText(getActivity(),"ok",Toast.LENGTH_SHORT).show();

                                OneSignal.postNotification(new JSONObject("{'contents': {'en': '"+etnotificationmsg.getText().toString()+"'}," +"'included_segments':[\"Subscribed Users\"],}"),

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

                        }
                    }
                });*/
                NavHostFragment.findNavController(AdminHome.this).navigate(R.id.action_adminHome_to_chatList);

            }
        });


        setdatetohome();
        setmyorderitem();
        setordercount();
        setAllitemcount();
        setCatitemcount();
        loadshareimg();

       return view;
    }

    private void setCatitemcount() {

        allcat.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tvnoofcat.setText(String.valueOf(snapshot.getChildrenCount()-1));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setAllitemcount() {
       allitemsref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tvnoofallitems.setText(String.valueOf(snapshot.getChildrenCount()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setordercount(){
        adminorder.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               tvnoorder.setText(String.valueOf(snapshot.getChildrenCount()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void setdatetohome(){
        Date cd = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
         formattedDate = df.format(cd);
        tvdate.setText(formattedDate);
    }

    private void setmyorderitem() {
        String date = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(new Date());
        Query query =adminorder.orderByChild("orderdate").startAt(date).endAt(date+ "\uf8ff");
        FirebaseRecyclerOptions<OrderDetails> options =
                new FirebaseRecyclerOptions.Builder<OrderDetails>()
                        .setQuery(query, OrderDetails.class)
                        .build();
        adapter = new FirebaseRecyclerAdapter<OrderDetails, AdminOrderViewHOLDER>(options) {

            @NonNull
            @Override
            public AdminOrderViewHOLDER onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adminorderlayout, parent, false);
                return new AdminOrderViewHOLDER(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull AdminOrderViewHOLDER holder, int position, @NonNull OrderDetails model) {
                holder.orderid.setText(model.getOrderid());
                holder.orderstatus.setText(model.getOrderstatus());
                holder.ordertotalprice.setText("\u20B9"+model.getOrdertotalprice());
                holder.ordertime.setText(model.getOrdertime());
                holder.paymentoption.setText(model.getPaymentoption());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle bundle=new Bundle();
                        bundle.putString("orderid",model.getOrderid());
                        bundle.putString("useruid",model.getUserid());
                        navController.navigate(R.id.action_adminHome_to_allOrderItemsAdmin,bundle);
                    }
                });

                String status=model.getOrderstatus();

                switch (status){
                    case "Pending":
                        holder.orderstatus.setTextColor(Color.RED);
                        break;
                    case "Confirm":
                        holder.orderstatus.setTextColor(getResources().getColor(R.color.yellow));
                        break;
                    case "Out for Delivery":
                        holder.orderstatus.setTextColor(getResources().getColor(R.color.skyblue));
                        break;
                    case "Delivered":
                        holder.orderstatus.setTextColor(getResources().getColor(R.color.teal_700));
                        break;
                }
            }
        };
        todaysorder.setAdapter(adapter);
        adapter.startListening();
    }

    public void loadshareimg() {
        imageshareviewadmin.setImageDrawable(getResources().getDrawable(R.drawable.chandanlogo));
        bitmapDrawable = (BitmapDrawable) imageshareviewadmin.getDrawable();
        bitmap = bitmapDrawable.getBitmap();

    }


    public void invite() {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/jpeg");
        Uri bmpuri;
        int happy=0x1F929;
        int hand=0x1F449;
        String text = "Hi download this app for Chandan App"+getEmojiByUnicode(hand)+"https://play.google.com/store/apps/details?id=" + getActivity().getPackageName() + "&hl=it";
        bmpuri = saveImage(bitmap, getActivity());
        share.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        share.putExtra(Intent.EXTRA_STREAM, bmpuri);
        share.putExtra(Intent.EXTRA_SUBJECT, "share App");
        share.putExtra(Intent.EXTRA_TEXT, text);
        startActivity(Intent.createChooser(share, "Share to"));
    }

    private static Uri saveImage(Bitmap image, Context context) {

        File imageFolder = new File(context.getCacheDir(), "images");
        Uri uri = null;
        try {
            imageFolder.mkdirs();
            File file = new File(imageFolder, "shared_images.jpg");
            FileOutputStream stream = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.JPEG, 90, stream);
            stream.flush();
            stream.close();
            uri = FileProvider.getUriForFile(Objects.requireNonNull(context.getApplicationContext()),
                    "com.reemzet.chandan" + ".provider", file);

        } catch (IOException e) {

        }
        return uri;
    }

    public String getEmojiByUnicode(int unicode){
        return new String(Character.toChars(unicode));
    }

}