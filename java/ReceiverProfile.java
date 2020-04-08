package com.example.android.meme;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ReceiverProfile extends AppCompatActivity {

    private TextView mReceiverName,userType;
    private TextView mReceiverEmail,mReceiverENo;
    private TextView mReceiverBatch,mReceiverCollege;
    private ImageView receiverPhoto;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiver_profile);
        //requestWindowFeature( Window.FEATURE_NO_TITLE );

        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN );
        Bundle bundle = getIntent().getExtras();
        setTitle(bundle.getCharSequence("RName"));

        mReceiverName = findViewById(R.id.recievers_name);
        mReceiverEmail = findViewById(R.id.receivers_email);
        mReceiverENo = findViewById(R.id.receiver_enrollment_no);
        receiverPhoto = findViewById(R.id.receiver_photo);
        mReceiverCollege = findViewById(R.id.recievers_college);
        mReceiverBatch = findViewById(R.id.recievers_batch);
        userType = findViewById(R.id.user_type);

        String receiverID = bundle.getString("rID");
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(receiverID);

       mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mReceiverBatch.setText(dataSnapshot.child("uBatch").getValue().toString());
                mReceiverName.setText(dataSnapshot.child("uFirstName").getValue().toString() + " " + dataSnapshot.child("uLastName").getValue().toString());
                mReceiverEmail.setText(dataSnapshot.child("uEmail").getValue().toString());
                mReceiverENo.setText(dataSnapshot.child("uID").getValue().toString());
                mReceiverCollege.setText(dataSnapshot.child("uCollege").getValue().toString());
                userType.setText("("+dataSnapshot.child("uUserType").getValue().toString()+")");
                if(dataSnapshot.hasChild("userPhoto")) {
                    String photo = dataSnapshot.child("userPhoto").getValue().toString();
                    Glide.with(getApplicationContext()).load(photo).into(receiverPhoto);
                }
                else {
                    receiverPhoto.setImageDrawable(getDrawable(R.drawable.profile_icon));
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}