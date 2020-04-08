package com.example.android.meme;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.google.firebase.database.FirebaseDatabase.getInstance;


public class MainActivity extends Activity {

    private EditText message;
    private DatabaseReference mDatabaseCurrent, mDatabaseReceiver, readMessageFromDB;
    private RecyclerView mMessageList;
    private FirebaseAuth mAuth;
    private Bundle bundle;
    private TextView receiverName;
    private ImageView addMedia;
    private TextView mEnrollmentNo;
    private CircleImageView profilePhoto;
    private ImageView chat_back;
    private LinearLayout mUserDataTab;
    private Uri imageUri;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();


        message = findViewById(R.id.edit_message);
        addMedia = findViewById(R.id.add_media);
        chat_back = findViewById(R.id.chat_back);
        bundle = new Bundle();
        bundle = getIntent().getExtras();


        profilePhoto = findViewById(R.id.reciever_profile_photo);
        if(bundle.getString("photo")==null)
        {
         profilePhoto.setImageDrawable(getDrawable(R.drawable.profile_icon));
        }
        else {
            Glide.with(getApplicationContext()).load(bundle.getString("photo")).into(profilePhoto);
        }

        receiverName = findViewById(R.id.reciever_name);
        receiverName.setText(bundle.getString("rName"));

        mEnrollmentNo = findViewById(R.id.receivers_enrollment_no);
        mEnrollmentNo.setText(bundle.getString("rEnrollmentNo").toUpperCase());

        mUserDataTab = findViewById(R.id.reciever_info_display);
        mUserDataTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viewReceieverProfile = new Intent(getApplicationContext(),ReceiverProfile.class);
                Bundle rInfo = new Bundle();
                rInfo.putString("rID",bundle.getString("rId"));
                rInfo.putString("RName",bundle.getString("rName"));
                viewReceieverProfile.putExtras(rInfo);
                startActivity(viewReceieverProfile);
            }
        });

        chat_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
            }
        });

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        //recycler view
        mMessageList = findViewById(R.id.message_list);
        mMessageList.setLayoutManager(linearLayoutManager);


        mDatabaseCurrent = getInstance().getReference().child("Message").child(mAuth.getCurrentUser().getUid());

        mDatabaseReceiver = getInstance().getReference().child("Message").child(bundle.getString("rId"));


        readMessageFromDB = getInstance().getReference().child("Message").child(mAuth.getCurrentUser().getUid());

        mDatabaseCurrent.keepSynced(true);
        mDatabaseReceiver.keepSynced(true);
        readMessageFromDB.keepSynced(true);
        mMessageList.setHasFixedSize(true);

        final FirebaseRecyclerAdapter<Message, MessageViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Message, MessageViewHolder>(Message.class, R.layout.message_display_layout,MessageViewHolder.class,mDatabaseCurrent.child(bundle.getString("rId")))
                {
                    @Override
                    protected void populateViewHolder(MessageViewHolder viewHolder, Message model, int position) {
                        if (model.getSender().equals(mAuth.getCurrentUser().getUid())) {

                            LinearLayout.LayoutParams layoutParams =
                                    new LinearLayout.LayoutParams
                                            (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                            layoutParams.leftMargin = (int) ((getWindowManager().getDefaultDisplay().getWidth())/(1.7));
                            viewHolder.mView.setBackground(getDrawable(R.drawable.message_content2));

                            try
                            {
                                layoutParams.leftMargin -= 20;
                            }
                            catch (Exception e) {

                            }
                            viewHolder.mView.setLayoutParams(layoutParams);
                            viewHolder.messageContent.setText(model.getContent());
                            viewHolder.timeDate.setText(model.getDateTime());
                            viewHolder.sender.setText("You");
                        } else {
                            viewHolder.mView.setBackground(getDrawable(R.drawable.message_content_display));
                            viewHolder.messageContent.setText(model.getContent());
                            viewHolder.timeDate.setText(model.getDateTime());
                            viewHolder.sender.setText(bundle.getString("rName"));
                        }
                    }
                };
        mMessageList.setAdapter(firebaseRecyclerAdapter);

        addMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(galleryIntent, "Select image"), 1);
            }
        });

    }



    void sendMedia()
    {
        Random random = new Random();
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("chats/images/"
                +mAuth.getCurrentUser().getUid()+"/"+random.nextInt(10000));
        storageReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                String dateAndTime = DateFormat.getDateTimeInstance().format(new Date());
                Map<String, Object> chatData = new HashMap<>();
                chatData.put("Content",imageUri);
                chatData.put("msgType","image");
                chatData.put("dateTime", dateAndTime);
                chatData.put("sender",mAuth.getCurrentUser().getUid());
                chatData.put("receiver",bundle.getCharSequence("rId"));

                mDatabaseCurrent.child(bundle.getString("rId")).push().setValue(chatData);
                mDatabaseReceiver.child(mAuth.getCurrentUser().getUid()).push().setValue(chatData);

              //  Toast.makeText(getApplicationContext(),"added image",Toast.LENGTH_LONG).show();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == 1 && resultCode == RESULT_OK)
        {
             imageUri = data.getData();
             Toast.makeText(getApplicationContext(),"image selected",Toast.LENGTH_LONG).show();
            sendMedia();
        }
    }

    public void sendButtonClicked(View view){
        final String msgValue = message.getText().toString().trim();
        if(!TextUtils.isEmpty(msgValue)){

            String dateAndTime = DateFormat.getDateTimeInstance().format(new Date());
            Map<String, Object> chatData = new HashMap<>();
            chatData.put("Content",msgValue);
            chatData.put("dateTime", dateAndTime);
            chatData.put("sender",mAuth.getCurrentUser().getUid());
            chatData.put("receiver",bundle.getCharSequence("rId"));

            mDatabaseCurrent.child(bundle.getString("rId")).push().setValue(chatData);
            mDatabaseReceiver.child(mAuth.getCurrentUser().getUid()).push().setValue(chatData);
            mMessageList.scrollToPosition(0);

            message.setText("");
        }
    }

    static public class MessageViewHolder extends RecyclerView.ViewHolder{

        View mView;
        TextView messageContent,timeDate,sender;
        public MessageViewHolder(View itemView) {
            super(itemView);
            mView=itemView;
            messageContent = mView.findViewById(R.id.message_text);
            timeDate = mView.findViewById(R.id.time);
            sender = mView.findViewById(R.id.sender);
        }
    }
}
