package com.reemzet.chandan.adminfragment;

import static android.content.ContentValues.TAG;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.airbnb.lottie.L;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.reemzet.chandan.Adapter.CategorylistAdapter;
import com.reemzet.chandan.Models.ItemDetails;
import com.reemzet.chandan.R;
import com.reemzet.chandan.fragment.Home;
import com.reemzet.chandan.fragment.Menu;
import com.reemzet.chandan.fragment.MyOrders;

import java.util.ArrayList;
import java.util.Locale;

public class AddItems extends Fragment {

    ArrayList<String> tag;
    FirebaseFirestore db;
    String keyword="";
    ItemDetails itemDetailswithtag;
    ItemDetails itemDetails;
    String itemname,itemcat,itemexam,itemdesc,itemimg,itemmrp,itemprice,itemtype,itemid;
    DatabaseReference itemref,itemcatref;
    FirebaseDatabase database;
    Button submitbtn;
    EditText etitemname,etitemdesc,etitemprice,etitemmrp,etitemimgurl;
    RadioButton rdnew,rdold;
    AutoCompleteTextView autocat1,autocat2;
    boolean fieldboolean;
    RadioGroup rdgroup;
    Toolbar toolbar;
    NavController navController;
    ImageView imgokbtn,itembookimg;
   String gdrivecode="https://drive.google.com/uc?export=download&id=";
   String exculdecode1="https://drive.google.com/file/d/";
   String exculdecode2="/view?usp=sharing";
   String exculdecode3="/view?usp=drivesdk";
    ArrayList<String> catlist;
    ProgressDialog progressDialog;
    String incomingbundle;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view= inflater.inflate(R.layout.fragment_add_items, container, false);
        db = FirebaseFirestore.getInstance();
        database=FirebaseDatabase.getInstance();
        submitbtn=view.findViewById(R.id.submititem);
        etitemname=view.findViewById(R.id.etitemname);
        etitemdesc=view.findViewById(R.id.etitemdesc);
        etitemprice=view.findViewById(R.id.etitemprice);
        etitemmrp=view.findViewById(R.id.etitemmrp);
        etitemimgurl=view.findViewById(R.id.etitemurl);
        rdnew=view.findViewById(R.id.rdnew);
        rdold=view.findViewById(R.id.rdold);
        autocat1=view.findViewById(R.id.autocat1);
        autocat2=view.findViewById(R.id.autocat2);
        rdgroup=view.findViewById(R.id.rdgroup);
        imgokbtn=view.findViewById(R.id.imgokbtn);
        itembookimg=view.findViewById(R.id.itembookimg);

        NavHostFragment navHostFragment = (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        assert navHostFragment != null;
        navController = navHostFragment.getNavController();

        itemref=database.getReference("App").child("Items");
        incomingbundle=getArguments().getString("editoradd");




         itemDetailswithtag=new ItemDetails();
         itemDetails=new ItemDetails();

        tag=new ArrayList<>();
        catlist=new ArrayList<>();

        toolbar=getActivity().findViewById(R.id.toolbar);
        if (incomingbundle.equals("edit")){
            toolbar.setTitle("Edit Item");
            itemimg=getArguments().getString("itemimg");
            itemname=getArguments().getString("itemtitle");
            itemcat=getArguments().getString("itemcat");
            itemdesc=getArguments().getString("itemdesc");
            itemprice=getArguments().getString("itemprice");
            itemmrp=getArguments().getString("itemmrp");
            itemtype=getArguments().getString("itemstatus");
            itemexam=getArguments().getString("itemexam");
            itemid=getArguments().getString("itemid");
            etitemname.setText(itemname);
            etitemdesc.setText(itemdesc);
            etitemimgurl.setText(itemimg);
            etitemmrp.setText(itemmrp);
            etitemprice.setText(itemprice);
            autocat1.setText(itemcat);
            autocat2.setText(itemexam);
            Glide.with(getActivity())
                    .load(itembookimg)
                    .centerCrop()
                    .placeholder(R.drawable.books)
                    .into(itembookimg);
            if (itemtype.equals("New")){
                rdnew.setChecked(true);
            }else {
                rdold.setChecked(true);
            }


        }else {
            toolbar.setTitle("Add Item");
            rdnew.setChecked(true);
        }

        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);

        toolbar.setNavigationOnClickListener(view1 -> {
            navController.popBackStack(R.id.adminHome,true);
            NavHostFragment.findNavController(AddItems.this).navigate(R.id.adminHome);
        });



        imgokbtn.setOnClickListener(view12 -> {
            if (!etitemimgurl.getText().toString().isEmpty()){
                String imgurl1=etitemimgurl.getText().toString().replace(exculdecode1,gdrivecode);
                String imgurl2=imgurl1.replace(exculdecode2,"");
                String finalimgurl=imgurl2.replace(exculdecode3,"");
                etitemimgurl.setText(finalimgurl);
                itemimg=finalimgurl;

                Glide.with(getActivity())
                        .load(finalimgurl)
                        .centerCrop()
                        .placeholder(R.drawable.books)
                        .into(itembookimg);

            }
        });
        submitbtn.setOnClickListener(view13 -> {
            if (!incomingbundle.equals("edit")){
                itemid=(itemref.push().getKey());
            }
            if ( checkandgetdatafromfield()){
                showloding();
                taggenerator(itemname+" "+itemcat+" "+itemexam);
                setdatatomodel();
                uploadtorealtimedb();
                uplloadtofirestore();


            }
        });
        itemref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()){
                    if (!ds.getKey().equals("itemlist")){
                        catlist.add(ds.getKey());
                    }
                    ArrayAdapter<String>adapter= new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, catlist);
                    autocat1.setAdapter(adapter);
                    autocat2.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

       return view;
    }

    private boolean checkandgetdatafromfield() {
        if (etitemname.getText().toString().isEmpty()){
            etitemname.setError("Can't be empty");
            etitemname.requestFocus();
            fieldboolean=false;
        }else if (etitemdesc.getText().toString().isEmpty()){
            etitemdesc.setError("Can't be empty");
            etitemdesc.requestFocus();
            fieldboolean=false;
        }else if (etitemmrp.getText().toString().isEmpty()){
            etitemmrp.setError("Can't be empty");
            etitemmrp.requestFocus();
            fieldboolean=false;
        }else if (etitemprice.getText().toString().isEmpty()){
            etitemprice.setError("Can't be empty");
            etitemmrp.requestFocus();
            fieldboolean=false;
        }else if (etitemimgurl.getText().toString().isEmpty()){
            etitemimgurl.setError("Can't be empty");
            etitemimgurl.requestFocus();
            fieldboolean=false;
        }else if (autocat1.getText().toString().isEmpty()){
            autocat1.setError("Can't be empty");
            autocat1.requestFocus();
            fieldboolean=false;
        }else if (autocat2.getText().toString().isEmpty()){
            autocat2.setError("Can't be empty");
            autocat2.requestFocus();
            fieldboolean=false;

        }else {
            itemname=etitemname.getText().toString();
            itemdesc=etitemdesc.getText().toString();
            itemmrp=etitemmrp.getText().toString();
            itemprice=etitemprice.getText().toString();

            itemcat=autocat1.getText().toString();
            itemexam=autocat2.getText().toString();
            if (rdgroup.getCheckedRadioButtonId() == R.id.rdnew){
                itemtype="New";
            }else itemtype="Old";
            fieldboolean=true;
        }
        return fieldboolean;
    }

    private void uplloadtofirestore() {
        db.collection("Items")
                .document(itemid)
                .set(itemDetailswithtag)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        etitemname.getText().clear();
                        etitemdesc.getText().clear();
                        etitemprice.getText().clear();
                        etitemmrp.getText().clear();
                        etitemimgurl.getText().clear();
                    }
                });
    }

    private void uploadtorealtimedb() {
        itemref.child("itemlist").child(itemid).setValue(itemDetails);

        if (!itemDetails.getItemcat().equals("no")){
            itemref.child(itemDetails.getItemcat()).setValue(itemDetails.getItemcat());
        }
        if (!itemDetails.getItemexam().equals("no")){
            itemref.child(itemDetails.getItemexam()).setValue(itemDetails.getItemcat());
        }
    }

    private void setdatatomodel() {
        itemDetails.setItemtitle(itemname);
        itemDetails.setItemavail("In Stock");
        itemDetails.setItemcat(itemcat);
        itemDetails.setItemexam(itemexam);
        itemDetails.setItemstatus(itemtype);
        itemDetails.setItemmrp(itemmrp);
        itemDetails.setItemprice(itemprice);
        itemDetails.setItemdesc(itemdesc);
        itemDetails.setItemimg(itemimg);
        itemDetails.setItemid(itemid);

        itemDetailswithtag.setItemtitle(itemname);
        itemDetailswithtag.setItemavail("In Stock");
        itemDetailswithtag.setItemcat(itemcat);
        itemDetailswithtag.setItemexam(itemexam);
        itemDetailswithtag.setItemstatus(itemtype);
        itemDetailswithtag.setItemmrp(itemmrp);
        itemDetailswithtag.setItemprice(itemprice);
        itemDetailswithtag.setItemdesc(itemdesc);
        itemDetailswithtag.setItemimg(itemimg);
        itemDetailswithtag.setItemtag(tag);
        itemDetailswithtag.setItemid(itemid);
    }

    private void taggenerator(String str) {

        String[] word=str.split(" ");

        for (int n=0;n<word.length;n++){
            for(int i=0;i<word[n].length();i++){
                for(int j=0;j<=i;j++){
                    keyword=keyword+(String.valueOf(word[n].toLowerCase(Locale.ROOT).charAt(j)));
                }
                tag.add(keyword);
                keyword="";
            }
        }

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