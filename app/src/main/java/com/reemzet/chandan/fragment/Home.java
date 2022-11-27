package com.reemzet.chandan.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.reemzet.chandan.Adapter.BookViewHolder;
import com.reemzet.chandan.Adapter.OfferViewHolder;
import com.reemzet.chandan.Adapter.ViewPagerAdapter;


import com.reemzet.chandan.Models.ItemDetails;
import com.reemzet.chandan.Models.OfferCode;
import com.reemzet.chandan.Models.ViewpagerModel;
import com.reemzet.chandan.R;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class Home extends Fragment {
    ViewPager2 viewPager2;
    Handler slideHandler = new Handler();
    DatabaseReference slider,offerref,recentbookref,storedetails,cartref;
    ShimmerFrameLayout shimmerFrameLayout,shimmerecent,shimmeroffer;
    FirebaseRecyclerAdapter<OfferCode, OfferViewHolder> adapter;
    FirebaseRecyclerAdapter<ItemDetails, BookViewHolder> booklistadapter;
    RecyclerView offerrecycler;
    List<String> tag;
    RecyclerView recentrecycler;
    Query query;
    FirebaseFirestore db;
    ArrayList<ViewpagerModel> viewpagerModelArrayList;

    NavController navController;
    TextView tvviewall;
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    TextView callbtn;
    LinearLayout booklinearlayout,invite,noteslayout,novellayout,cartlayout,calssxlayout,clssxixiilayout,engglayout,medicallayout,generallayout,layoutnovel,quebanklayout,kidslayout,statioarylayout,practicesetlayout,paperslayout,magazinelayout,ssclayout,bpsclayout,upsclayout,railwaylayout,jeemainslayout,neetlayout,ndalayout,gatelayout;
    TextView textCartItemCount;
    int mCartItemCount = 0;
    TextView searchtext;
    ProgressDialog progressDialog;
    BitmapDrawable bitmapDrawable;
    Bitmap bitmap;
    ImageView imageshareview;
    String latitude,longititude;
    FirebaseAuth mAuth;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view=inflater.inflate(R.layout.fragment_home, container, false);
        shimmerFrameLayout = view.findViewById(R.id.shimmer_view_container);
        toolbar = getActivity().findViewById(R.id.toolbar);
        drawerLayout=getActivity().findViewById(R.id.drawer);
        viewPager2 = view.findViewById(R.id.sliderviewpager);
        offerrecycler=view.findViewById(R.id.offerrecycler);

        recentrecycler=view.findViewById(R.id.recentrecycler);
        shimmerecent=view.findViewById(R.id.shimmerrecent);
        shimmeroffer=view.findViewById(R.id.shimmeroffer);
        searchtext=view.findViewById(R.id.searchtext);
        booklinearlayout=view.findViewById(R.id.booklinearlayout);
        noteslayout=view.findViewById(R.id.noteslayout);
        novellayout=view.findViewById(R.id.novellayout);
        cartlayout=view.findViewById(R.id.cartlayout);
        calssxlayout=view.findViewById(R.id.classxlayout);
        clssxixiilayout=view.findViewById(R.id.classxixiilayout);
        engglayout=view.findViewById(R.id.englayout);
        medicallayout=view.findViewById(R.id.medicallayout);
        generallayout=view.findViewById(R.id.generallayout);
        layoutnovel=view.findViewById(R.id.layoutnovel);
        quebanklayout=view.findViewById(R.id.quebanklayout);
        kidslayout=view.findViewById(R.id.kidslayout);
        statioarylayout=view.findViewById(R.id.stationarylayout);
        practicesetlayout=view.findViewById(R.id.practicesetlayout);
        paperslayout=view.findViewById(R.id.paperslayout);
        magazinelayout=view.findViewById(R.id.magazinelayout);
        ssclayout=view.findViewById(R.id.ssclayout);
        upsclayout=view.findViewById(R.id.upsclayout);
        bpsclayout=view.findViewById(R.id.bpsclayout);
        jeemainslayout=view.findViewById(R.id.jeemainslayout);
        neetlayout=view.findViewById(R.id.neetlayout);
        ndalayout=view.findViewById(R.id.ndalayout);
        gatelayout=view.findViewById(R.id.gatelayout);
        railwaylayout=view.findViewById(R.id.raillayout);
        tvviewall=view.findViewById(R.id.tvviewall);
        callbtn=view.findViewById(R.id.tvcallbtn);
        imageshareview=view.findViewById(R.id.imageshareviewhome);
        invite=view.findViewById(R.id.invite);
        textCartItemCount=getActivity().findViewById(R.id.tvcartcount);



        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database=FirebaseDatabase.getInstance();
        mAuth=FirebaseAuth.getInstance();
        slider = database.getReference("App/Slider");
        offerref=database.getReference("App").child("offer");
        recentbookref=database.getReference("App").child("Items");
        storedetails=database.getReference("App").child("storedetails");
        cartref=database.getReference("App/user");
        db = FirebaseFirestore.getInstance();
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL);

        staggeredGridLayoutManager.setReverseLayout(true);




        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,true);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recentrecycler.setLayoutManager(linearLayoutManager);

        LinearLayoutManager offerlinearLayoutManager = new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,true);
        offerlinearLayoutManager.setReverseLayout(true);
        offerlinearLayoutManager.setStackFromEnd(true);
        offerrecycler.setLayoutManager(offerlinearLayoutManager);
        NavHostFragment navHostFragment = (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        assert navHostFragment != null;
        navController = navHostFragment.getNavController();

        shimmerFrameLayout.startShimmer();
        slidermethod();
       setofferdata();
       setrecentbook();
       buttonclick();
       loadshareimg();
       setupBadge();



        toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);
        toolbar.setTitle("Chandan");
        toolbar.setNavigationIcon(R.drawable.ic_baseline_menu_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                drawerLayout.open();

            }
        });
        callbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent my_callIntent = new Intent(Intent.ACTION_DIAL);
                    my_callIntent.setData(Uri.parse("tel:"+"7766816686"));
                    //here the word 'tel' is important for making a call...
                    startActivity(my_callIntent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getActivity(), "Error in your phone call"+e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
        invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                invite();
            }
        });



       return view;
    }


    public void setofferdata(){

        FirebaseRecyclerOptions<OfferCode> options =
                new FirebaseRecyclerOptions.Builder<OfferCode>()
                        .setQuery(offerref, OfferCode.class)
                        .build();



        adapter=new FirebaseRecyclerAdapter<OfferCode,OfferViewHolder>(options) {


            @NonNull
            @Override
            public OfferViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View offerview = LayoutInflater.from(parent.getContext()).inflate(R.layout.offerlayout, parent, false);
                return new OfferViewHolder(offerview);
            }

            @Override
            protected void onBindViewHolder(@NonNull OfferViewHolder holder, int position, @NonNull OfferCode model) {
                holder.offername.setText(model.getOffername());
                holder.promocode.setText(model.getPromocode());
                shimmeroffer.stopShimmer();
                shimmeroffer.setVisibility(View.GONE);
                holder.promocode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ClipboardManager clipboardManager= (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                        clipboardManager.setText(holder.promocode.getText());
                        Toast.makeText(getActivity(), "Copied", Toast.LENGTH_SHORT).show();

                    }
                });

            }
        };
        offerrecycler.setAdapter(adapter);
        adapter.startListening();
    }

    public void setrecentbook(){
        query = recentbookref.child("itemlist").limitToLast(6);
        FirebaseRecyclerOptions<ItemDetails> options =
                new FirebaseRecyclerOptions.Builder<ItemDetails>()
                        .setQuery(query, ItemDetails.class)
                        .build();

        booklistadapter=new FirebaseRecyclerAdapter<ItemDetails, BookViewHolder>(options) {
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
                    bundle.putString("itemid",model.getItemid());
                    navController.navigate(R.id.action_home2_to_bookDescPage,bundle);

                });
                shimmerecent.stopShimmer();
                shimmerecent.setVisibility(View.GONE);
                recentrecycler.setVisibility(View.VISIBLE);
            }

            @NonNull
            @Override
            public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View bookview = LayoutInflater.from(parent.getContext()).inflate(R.layout.bookitemlayout, parent, false);
                return new BookViewHolder(bookview);
            }
        };
        recentrecycler.setAdapter(booklistadapter);
        booklistadapter.startListening();
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
                viewPager2.setAdapter(new ViewPagerAdapter(getContext(), viewpagerModelArrayList, viewPager2));

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
                        slideHandler.postDelayed(sliderRunnable, 3000);
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private final Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
        }
    };

    public void buttonclick(){
        booklinearlayout.setOnClickListener(view -> navController.navigate(R.id.action_home2_to_searchItem));
        searchtext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_home2_to_searchItem);
            }
        });
        novellayout.setOnClickListener(view -> {
            Bundle bundle=new Bundle();
            bundle.putString("category","Novel");
            navController.navigate(R.id.action_home2_to_categoryPage,bundle);
        });
        upsclayout.setOnClickListener(view -> {
            Bundle bundle=new Bundle();
            bundle.putString("category","UPSC");
            navController.navigate(R.id.action_home2_to_categoryPage,bundle);
        });
        tvviewall.setOnClickListener(view -> navController.navigate(R.id.listofCategory));
        cartlayout.setOnClickListener(view -> statusCheck());
        calssxlayout.setOnClickListener(view -> {
            Bundle bundle=new Bundle();
            bundle.putString("category","class x");
            navController.navigate(R.id.action_home2_to_categoryPage,bundle);
        });
        clssxixiilayout.setOnClickListener(view -> {
            Bundle bundle=new Bundle();
            bundle.putString("category","class xi/xii");
            navController.navigate(R.id.action_home2_to_categoryPage,bundle);
        });
        engglayout.setOnClickListener(view -> {
            Bundle bundle=new Bundle();
            bundle.putString("category","Engineering");
            navController.navigate(R.id.action_home2_to_categoryPage,bundle);
        });
        medicallayout.setOnClickListener(view -> {
            Bundle bundle=new Bundle();
            bundle.putString("category","medical");
            navController.navigate(R.id.action_home2_to_categoryPage,bundle);
        });
        generallayout.setOnClickListener(view -> {
            Bundle bundle=new Bundle();
            bundle.putString("category","General");
            navController.navigate(R.id.action_home2_to_categoryPage,bundle);
        });
        layoutnovel.setOnClickListener(view -> {
            Bundle bundle=new Bundle();
            bundle.putString("category","Novel");
            navController.navigate(R.id.action_home2_to_categoryPage,bundle);
        });
        quebanklayout.setOnClickListener(view -> {
            Bundle bundle=new Bundle();
            bundle.putString("category","Que Bank");
            navController.navigate(R.id.action_home2_to_categoryPage,bundle);
        });
        kidslayout.setOnClickListener(view -> {
            Bundle bundle=new Bundle();
            bundle.putString("category","Kids");
            navController.navigate(R.id.action_home2_to_categoryPage,bundle);
        });
        statioarylayout.setOnClickListener(view -> {
            Bundle bundle=new Bundle();
            bundle.putString("category","Stationary");
            navController.navigate(R.id.action_home2_to_categoryPage,bundle);
        });
        practicesetlayout.setOnClickListener(view -> {
            Bundle bundle=new Bundle();
            bundle.putString("category","Practice set");
            navController.navigate(R.id.action_home2_to_categoryPage,bundle);
        });
        paperslayout.setOnClickListener(view -> {
            Bundle bundle=new Bundle();
            bundle.putString("category","Papers");
            navController.navigate(R.id.action_home2_to_categoryPage,bundle);
        });
        magazinelayout.setOnClickListener(view -> {
            Bundle bundle=new Bundle();
            bundle.putString("category","Magazine");
            navController.navigate(R.id.action_home2_to_categoryPage,bundle);
        });
        ssclayout.setOnClickListener(view -> {
            Bundle bundle=new Bundle();
            bundle.putString("category","SSC");
            navController.navigate(R.id.action_home2_to_categoryPage,bundle);
        });
        bpsclayout.setOnClickListener(view -> {
            Bundle bundle=new Bundle();
            bundle.putString("category","BPSC");
            navController.navigate(R.id.action_home2_to_categoryPage,bundle);
        });
        railwaylayout.setOnClickListener(view -> {
            Bundle bundle=new Bundle();
            bundle.putString("category","Railway");
            navController.navigate(R.id.action_home2_to_categoryPage,bundle);
        });
        jeemainslayout.setOnClickListener(view -> {
            Bundle bundle=new Bundle();
            bundle.putString("category","JEE Mains");
            navController.navigate(R.id.action_home2_to_categoryPage,bundle);
        });
        neetlayout.setOnClickListener(view -> {
            Bundle bundle=new Bundle();
            bundle.putString("category","NEET");
            navController.navigate(R.id.action_home2_to_categoryPage,bundle);
        });
        ndalayout.setOnClickListener(view -> {
            Bundle bundle=new Bundle();
            bundle.putString("category","NDA");
            navController.navigate(R.id.action_home2_to_categoryPage,bundle);
        });
        gatelayout.setOnClickListener(view -> {
            Bundle bundle=new Bundle();
            bundle.putString("category","GATE");
            navController.navigate(R.id.action_home2_to_categoryPage,bundle);
        });
        noteslayout.setOnClickListener(view -> {
            Bundle bundle=new Bundle();
            bundle.putString("category","Notes");
            navController.navigate(R.id.action_home2_to_categoryPage,bundle);
        });

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


    public void statusCheck() {
        final LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        }else {
            Dexter.withContext(getActivity())
                    .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    .withListener(new PermissionListener() {
                        @Override
                        public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                            showloding();
                            storedetails.child("location").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()){
                                        progressDialog.dismiss();
                                        latitude=snapshot.child("latitude").getValue(String.class);
                                        longititude=snapshot.child("longitude").getValue(String.class);
                                        Bundle bundle=new Bundle();
                                        bundle.putDouble("latitudeloc",Double.valueOf(latitude));
                                        bundle.putDouble("longitutdeloc",Double.valueOf(longititude));
                                        navController.navigate(R.id.action_home2_to_cartFragment,bundle);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });


                        }

                        @Override
                        public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                            Toast.makeText(getActivity(),"you can not access cart without location",Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                            permissionToken.continuePermissionRequest();
                        }
                    }).check();
        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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


    public void loadshareimg() {
        imageshareview.setImageDrawable(getResources().getDrawable(R.drawable.chandanlogo));
        bitmapDrawable = (BitmapDrawable) imageshareview.getDrawable();
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
    public void showloding() {
        if (getActivity() != null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.show();
            progressDialog.setContentView(R.layout.dialoprogress);
            progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            progressDialog.setCanceledOnTouchOutside(false);
        }
    }
}











