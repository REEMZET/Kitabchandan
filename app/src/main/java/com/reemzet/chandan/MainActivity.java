package com.reemzet.chandan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.onesignal.OneSignal;
import com.reemzet.chandan.Models.UserModel;

import org.imaginativeworld.oopsnointernet.callbacks.ConnectionCallback;
import org.imaginativeworld.oopsnointernet.dialogs.signal.DialogPropertiesSignal;
import org.imaginativeworld.oopsnointernet.dialogs.signal.NoInternetDialogSignal;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;
import java.util.Scanner;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    Toolbar toolbar;
    NavigationView navigationView;
    NavController navController;
    TextView textCartItemCount;
    int mCartItemCount = 0;

    TextView loginusername, loginuserphonno;
    LinearLayout myorder,profile,share,logout,checkupdate,aboutdev,aboutorg;
    ImageView loginuserpic;

    ProgressDialog progressDialog;
    FirebaseDatabase database;
    DatabaseReference useref,storedetailsref,cartref,updatref;
    UserModel userModel;
    FirebaseAuth mAuth;
    ImageView imageshare,carttoolimg;
    BitmapDrawable bitmapDrawable;
    Bitmap bitmap;
    String latitude,longititude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        drawerLayout = findViewById(R.id.drawer);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.nav_view);
        database=FirebaseDatabase.getInstance();
        useref=database.getReference("App/user");
        storedetailsref=database.getReference("App/storedetails");
        cartref=database.getReference("App/user");
        updatref=database.getReference("App/update");
        mAuth=FirebaseAuth.getInstance();
        imageshare=findViewById(R.id.imageshare);
        carttoolimg=findViewById(R.id.carttoolimg);





        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        assert navHostFragment != null;

        navController = navHostFragment.getNavController();

        View headerView = navigationView.getHeaderView(0);
        loginusername = headerView.findViewById(R.id.loginusername);
        loginuserphonno = headerView.findViewById(R.id.loginuserphone);
        loginuserpic = headerView.findViewById(R.id.loginuserpic);
        myorder=headerView.findViewById(R.id.ll_myorder);
        profile=headerView.findViewById(R.id.ll_Profile);
        share=headerView.findViewById(R.id.ll_Share);
        logout=headerView.findViewById(R.id.ll_Logout);
        checkupdate=headerView.findViewById(R.id.ll_update);
        aboutdev=headerView.findViewById(R.id.ll_developer);
        aboutorg=headerView.findViewById(R.id.ll_organisation);
        textCartItemCount=findViewById(R.id.tvcartcount);
        NavigationUI.setupWithNavController(navigationView, navController);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setItemIconTintList(null);
        checkinternet();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setItemIconTintList(null);

        loadshareimg();
        checkAppupdate();

        myorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userModel.getAccounttype().equals("Admin")){
                    navController.navigate(R.id.allOrderItemsAdmin);
                }else {
                    navController.navigate(R.id.myOrders);
                }

            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userModel.getAccounttype().equals("Admin")){
                    Toast.makeText(getApplicationContext(),"You are Admin",Toast.LENGTH_SHORT).show();
                }else {
                    navController.navigate(R.id.userProfile);
                }

            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            invite();
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Alert!")
                        .setMessage("Are you Sure to logout?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mAuth.signOut();
                                navController.popBackStack();
                                navController.navigate(R.id.splashScreen);
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
        checkupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppUpdateChecker appUpdateChecker = new AppUpdateChecker(MainActivity.this);
                appUpdateChecker.checkForUpdate(true);
            }
        });
        aboutdev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putString("url","https://sites.google.com/view/reemzetdeveloper/home");
                navController.navigate(R.id.webViewPage,bundle);
            }
        });
        aboutorg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putString("url","https://sites.google.com/view/chandanpustakbhandar/home");
                navController.navigate(R.id.webViewPage,bundle);
            }
        });
    carttoolimg.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            statusCheck();
        }
    });






        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id=item.getItemId();
                    switch (id){
                        case R.id.profile:
                            navController.navigate(R.id.userProfile);
                            break;
                        case R.id.home2:
                            navController.navigate(R.id.home);
                            break;
                        case R.id.chat:
                            navController.navigate(R.id.chatFragment);
                            break;
                        case R.id.cartmenu:
                           statusCheck();
                            break;

                    }


                return false;
            }
        });
        
        navController.addOnDestinationChangedListener((navController, navDestination, bundle) -> {
            int id= navDestination.getId();
            switch (id){
                case R.id.sinUpfragment:
                case R.id.userRegisteration:
                case R.id.splashScreen:

                    toolbar.setVisibility(View.GONE);
                   drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                    break;
                case R.id.bookDescPage:
                case R.id.home2:
                    carttoolimg.setVisibility(View.VISIBLE);
                    textCartItemCount.setVisibility(View.VISIBLE);
                    break;
                default:
                    carttoolimg.setVisibility(View.GONE);
                    textCartItemCount.setVisibility(View.GONE);
                    toolbar.setVisibility(View.VISIBLE);
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            }
            if (navDestination.getId()== R.id.home2){
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                bottomNavigationView.setVisibility(View.VISIBLE);

            }else {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                bottomNavigationView.setVisibility(View.GONE);
            }
        });

        if (mAuth.getCurrentUser()!=null) {
            useref.child(mAuth.getUid()).child("deviceid").setValue(OneSignal.getDeviceState().getUserId());
        useref.child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    userModel=snapshot.getValue(UserModel.class);
                    loginusername.setText(userModel.getUsername());
                    loginuserphonno.setText(userModel.getUserphone());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        }


    }

    private void checkAppupdate() {
        updatref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    if (snapshot.getValue(String.class).equals("true")){
                        AppUpdateChecker appUpdateChecker = new AppUpdateChecker(MainActivity.this);
                        appUpdateChecker.checkForUpdate(true);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void statusCheck() {
        final LocationManager manager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        }else {
            Dexter.withContext(this)
                    .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    .withListener(new PermissionListener() {
                        @Override
                        public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                            showloding();
                            storedetailsref.child("location").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()){
                                        progressDialog.dismiss();
                                        latitude=snapshot.child("latitude").getValue(String.class);
                                        longititude=snapshot.child("longitude").getValue(String.class);
                                        Bundle bundle=new Bundle();
                                        bundle.putDouble("latitudeloc",Double.valueOf(latitude));
                                        bundle.putDouble("longitutdeloc",Double.valueOf(longititude));
                                        navController.navigate(R.id.cartFragment,bundle);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });


                        }

                        @Override
                        public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                            Toast.makeText(MainActivity.this,"you can not access cart without location",Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                            permissionToken.continuePermissionRequest();
                        }
                    }).check();
        }
    }

    public void checkinternet(){

        NoInternetDialogSignal.Builder builder = new NoInternetDialogSignal.Builder(
                this,
                getLifecycle()
        );

        DialogPropertiesSignal properties = builder.getDialogProperties();

        properties.setConnectionCallback(new ConnectionCallback() { // Optional
            @Override
            public void hasActiveConnection(boolean hasActiveConnection) {
                // ...

            }
        });

        properties.setCancelable(false); // Optional
        properties.setNoInternetConnectionTitle("No Internet"); // Optional
        properties.setNoInternetConnectionMessage("Check your Internet connection"); // Optional
        properties.setShowInternetOnButtons(true); // Optional
        properties.setPleaseTurnOnText("Please turn on"); // Optional
        properties.setWifiOnButtonText("Wifi"); // Optional
        properties.setMobileDataOnButtonText("Mobile data"); // Optional

        properties.setOnAirplaneModeTitle("No Internet"); // Optional
        properties.setOnAirplaneModeMessage("You have turned on the airplane mode."); // Optional
        properties.setPleaseTurnOffText("Please turn off"); // Optional
        properties.setAirplaneModeOffButtonText("Airplane mode"); // Optional
        properties.setShowAirplaneModeOffButtons(true); // Optional
        builder.build();

    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private void setupBadge() {

        if (mAuth.getCurrentUser()!=null) {
            cartref.child(mAuth.getUid()).child("usercart").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        mCartItemCount=Integer.valueOf((int) snapshot.getChildrenCount());
                        if (textCartItemCount != null) {
                            if (mCartItemCount == 0) {
                                if (textCartItemCount.getVisibility() != View.GONE) {
                                    textCartItemCount.setVisibility(View.GONE);
                                }
                            } else {
                                textCartItemCount.setText(String.valueOf(Math.min(mCartItemCount, 99)));
                                if (textCartItemCount.getVisibility() != View.VISIBLE) {
                                    textCartItemCount.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }



    }




    public void loadshareimg() {
        imageshare.setImageDrawable(getResources().getDrawable(R.drawable.chandanlogo));
        bitmapDrawable = (BitmapDrawable) imageshare.getDrawable();
        bitmap = bitmapDrawable.getBitmap();

    }


    public void invite() {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/jpeg");
        Uri bmpuri;
        int happy=0x1F929;
        int hand=0x1F449;
        String text = "Hi download this app for Chandan App"+getEmojiByUnicode(hand)+"https://play.google.com/store/apps/details?id=" + this.getPackageName() + "&hl=it";
        bmpuri = saveImage(bitmap, this);
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

    public void showloding() {
        if (this != null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.show();
            progressDialog.setContentView(R.layout.dialoprogress);
            progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            progressDialog.setCanceledOnTouchOutside(false);
        }
    }

}
