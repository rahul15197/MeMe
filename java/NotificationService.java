package com.example.android.meme;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

public class NotificationService extends Service {

    private Random rNumber;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase, mUserData;
    private String senderName;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /*
        private String getSenderName(String name)
        {
            mUserData = FirebaseDatabase.getInstance().getReference().child("Users").child(name);
            Log.d("aId : ",name);
            mUserData.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Iterable<DataSnapshot> sName = dataSnapshot.getChildren();
                  //  Log.d("aId datasnapshot : ",dataSnapshot.toString());
                    //while (sName.iterator().hasNext()) {
                      //  Log.d("iterator : ", sName.iterator().toString());
                    //}
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            return  senderName;
        }

        private void displayNotification(String name) {
            Log.d("auth id :", name);
            if (!(name.equals(mAuth.getCurrentUser().getUid().toString()))) {
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext());
                mBuilder.setSmallIcon(R.mipmap.meme_logo);
                Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                mBuilder.setSound(notificationSound);
                //if( getSenderName(name) != null) {
                 //   mBuilder.setContentTitle(getSenderName(name) + "sent you a message.");
                //} else {
                    mBuilder.setContentTitle("You have received a new message.");
                    getSenderName(name);
                //}
                // mBuilder.setContentText(msg);
               // mBuilder.setAutoCancel(true);
                mBuilder.getNotification().flags |= Notification.FLAG_AUTO_CANCEL;

                NotificationManager mNotificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                Intent forwardToChat = new Intent(getApplicationContext(), LoginActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),
                        (int) System.currentTimeMillis(), forwardToChat, 0);
                mBuilder.setContentIntent(pendingIntent);

                rNumber = new Random();

                mNotificationManager.notify(rNumber.nextInt(100), mBuilder.build());
            }
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            mAuth = FirebaseAuth.getInstance();
            final int[] i = {0};
            final HashMap<Integer,DataSnapshot> msgDetails;
            msgDetails = new HashMap<>();

            mDatabase = FirebaseDatabase.getInstance().getReference().child("Message").child(mAuth.getCurrentUser().getUid());
            mDatabase.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(final DataSnapshot dataSnapshot) {
                 /*   Iterable<DataSnapshot> senderID = dataSnapshot.getChildren();
                    while (senderID.iterator().hasNext()) {
                        int i=0;
                        HashMap<Integer,String> msgDetails = new HashMap<>();
                        msgDetails.put(i++, senderID.iterator().next().getValue().toString());
                        Log.d("hash map", senderID.iterator().toString());
                       // displayNotification(senderID.iterator().next().getChildren().iterator().next().child("sender").getValue().toString());
                   */
/*
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    msgDetails.put(i[0]++,dataSnapshot1);
                                    }
                Log.d("hash map",msgDetails.toString());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
*/
    @Override

    public int onStartCommand(Intent intent, int flags, int startId) {

        try {
            mAuth = FirebaseAuth.getInstance();
            if (!(mAuth.getCurrentUser().getUid().equals(intent.getStringExtra("rID")))) {
                mDatabase = FirebaseDatabase.getInstance().getReference().child("Message").child(mAuth.getCurrentUser().getUid()).child(intent.getStringExtra("rID"));
                mDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext());
                        mBuilder.setSmallIcon(R.mipmap.meme_logo);
                        Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                        mBuilder.setSound(notificationSound);
                        mBuilder.setContentTitle("New Message received");
                        mBuilder.setContentText("Tap to view the message!");
                        mBuilder.setAutoCancel(true);
                        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                        Intent forwardToChat = new Intent(getApplicationContext(), NoticeBoard.class);
                        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), (int) System.currentTimeMillis(), forwardToChat, 0);
                        mBuilder.setContentIntent(pendingIntent);

                        mNotificationManager.notify(1, mBuilder.build());

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        } catch (Exception exception) {

        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
