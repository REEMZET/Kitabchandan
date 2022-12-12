package com.reemzet.chandan.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.reemzet.chandan.Models.MessageModel;
import com.reemzet.chandan.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter {
    Context context;
    ArrayList<MessageModel> messageModels;
    final int ItemSent = 1;
    final int ItemRecieve = 2;
    final int AmdinRecieve=4;

    final int MsgDatechange=3;
    final int MsgNotDatechange=3;



    public MessageAdapter(Context context, ArrayList<MessageModel> messageModels) {
        this.context = context;
        this.messageModels = messageModels;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ItemSent) {
            View view = LayoutInflater.from(context).inflate(R.layout.sendmsglayout, parent, false);
            return new SentViewholder(view);
        }else if (viewType==AmdinRecieve){
            View view = LayoutInflater.from(context).inflate(R.layout.adminrecievelayout, parent, false);
            return new ReceiverViewholder(view);
        }

        else{
            View view = LayoutInflater.from(context).inflate(R.layout.recievemsglayout, parent, false);
            return new ReceiverViewholder(view);
        }


    }

    @Override
    public int getItemViewType(int position) {
        MessageModel message = messageModels.get(position);
        if (FirebaseAuth.getInstance().getUid().equals(message.getSenderuid())) {
            return ItemSent;

        }else if (message.getSenderaccounttype().equals("Admin")){
            return AmdinRecieve;
        }
        else{
            return ItemRecieve;
        }
}

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MessageModel message = messageModels.get(position);
        if (holder.getClass() == SentViewholder.class) {
            SentViewholder viewholder = (SentViewholder) holder;
            viewholder.sentextmsg.setText(message.getMessage());
            viewholder.msgdate.setText(message.getDate());

            viewholder.senderlayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    new AlertDialog.Builder(context)
                            .setTitle("Are you sure?")
                            .setMessage("Do you want to delete this message?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    DatabaseReference msg = database.getReference("App/chat");
                                    msg.child(message.getSenderuid()).child(message.getMessageid()).removeValue();
                                }
                            }).setNegativeButton("No", null).show();


                    return false;
                }
            });
            viewholder.sentextmsg.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    new AlertDialog.Builder(context)
                            .setTitle("Are you sure?")
                            .setMessage("Do you want to delete this message?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    DatabaseReference msg = database.getReference("App/chat");
                                    msg.child("public").child(message.getMessageid()).removeValue();
                                }
                            }).setNegativeButton("No", null).show();


                    return false;
                }
            });



            viewholder.timestamp.setText(message.getTimestamp());
        }
        else {
                ReceiverViewholder viewholder = (ReceiverViewholder) holder;
               /* viewholder.recieverimage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogPlus dialog = DialogPlus.newDialog(viewholder.recieverimage.getContext())
                                .setContentHolder(new ViewHolder(R.layout.senderdetails))
                                .setGravity(Gravity.CENTER)
                                .create();
                        dialog.show();
                        View myview = dialog.getHolderView();
                        TextView sendername=myview.findViewById(R.id.sendername);
                        TextView senderbranch=myview.findViewById(R.id.senderbranch);
                        TextView sendersem=myview.findViewById(R.id.sendersem);
                        TextView sendercollege=myview.findViewById(R.id.sendercollege);

                        CircleImageView senderimage=myview.findViewById(R.id.senderprofile);
                        sendername.setText(message.getSendername());
                        sendercollege.setText(message.getSenderroll());
                        senderbranch.setText(message.getSenderaccounttype());
                        sendersem.setText(message.getSendersem());
                        Glide.with(viewholder.recieverimage.getContext()).load(message.getProfilepic())
                                .placeholder(R.drawable.profile)
                                .into(senderimage);

                    }
                });*/

                viewholder.rectextmsg.setText(message.getMessage());
                viewholder.recivername.setText("@"+message.getSendername());
                viewholder.timestamp.setText(message.getTimestamp());
                viewholder.msgdate.setText(message.getDate());

            }
        }



    @Override
    public int getItemCount() {
        return messageModels.size();
    }



     public class SentViewholder extends RecyclerView.ViewHolder{
        TextView timestamp, sentextmsg,msgdate ;
        CircleImageView senderimage;
        LinearLayout senderlayout;

        public SentViewholder(@NonNull View itemView) {
            super(itemView);
            sentextmsg = itemView.findViewById(R.id.senttextmsg);
            timestamp = itemView.findViewById(R.id.timestamp);
            senderimage = itemView.findViewById(R.id.senderimage);
            msgdate=itemView.findViewById(R.id.msgdate);
            senderlayout=itemView.findViewById(R.id.senderlayout);
        }
    }


    public class ReceiverViewholder extends RecyclerView.ViewHolder {
        TextView timestamp, rectextmsg, recivername,msgdate;
        CircleImageView recieverimage;

        public ReceiverViewholder(@NonNull View itemView) {
            super(itemView);
            timestamp = itemView.findViewById(R.id.timestamp);
            rectextmsg = itemView.findViewById(R.id.rectextmsg);
            recieverimage = itemView.findViewById(R.id.reciverimage);
            recivername = itemView.findViewById(R.id.reciername);
            msgdate=itemView.findViewById(R.id.msgdate);


        }
    }

}



