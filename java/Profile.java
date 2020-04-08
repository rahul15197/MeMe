package com.example.android.meme;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import es.dmoral.toasty.Toasty;

public class Profile extends AppCompatActivity {
    public ImageView icon;
    private TextView profile_name,profile_email_text,profile_college_text,profile_id_text,profile_batch_text;
    public Button reset,logout;
    private FirebaseAuth fba;
    private DatabaseReference databaseReference;
    private ImageButton choose_image,upload_image;
    private ProgressBar progressBar;
    private Uri filepath,resulturi;
    private final int PICK_IMAGE_REQUEST=1;
    FirebaseStorage storage;
    StorageReference storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_profile);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        icon = (ImageView)findViewById(R.id.profile_image);
        reset = (Button)findViewById(R.id.profile_reset);
        logout = (Button)findViewById(R.id.profile_logout);
        progressBar = findViewById(R.id.profile_progress);
        choose_image = findViewById(R.id.choose_image);
        upload_image = findViewById(R.id.upload_image);
        profile_name = findViewById(R.id.profile_name);
        profile_email_text = findViewById(R.id.profile_email_text);
        profile_college_text = findViewById(R.id.profile_college_text);
        profile_id_text = findViewById(R.id.profile_id_text);
        profile_batch_text = findViewById(R.id.profile_batch_text);
        progressBar.setVisibility(View.VISIBLE);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        fba = FirebaseAuth.getInstance();
        String uid = user.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
        if(!isNetworkConnected()){
            progressBar.setVisibility(View.GONE);
            Toasty.warning(Profile.this, "Check Internet Connectivity", Toast.LENGTH_SHORT, true).show();
        }
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.GONE);
                profile_name.setText(dataSnapshot.child("uFirstName").getValue().toString() + " " + dataSnapshot.child("uLastName").getValue().toString());
                profile_email_text.setText(dataSnapshot.child("uEmail").getValue().toString());
                profile_college_text.setText(dataSnapshot.child("uCollege").getValue().toString());
                profile_id_text.setText(dataSnapshot.child("uID").getValue().toString());
                profile_batch_text.setText(dataSnapshot.child("uBatch").getValue().toString());
                if(dataSnapshot.hasChild("userPhoto"))
                {
                    String url = dataSnapshot.child("userPhoto").getValue().toString();
                    Glide.with(getApplicationContext()).load(url).into(icon);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fba.sendPasswordResetEmail(user.getEmail()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toasty.success(Profile.this, "Password Reset Email sent to your registered email", Toast.LENGTH_SHORT, true).show();
                        }else if(!isNetworkConnected()){
                            Toasty.warning(Profile.this, "Check Internet Connectivity", Toast.LENGTH_SHORT, true).show();
                        }
                        else
                            Toasty.error(Profile.this, "Something went wrong", Toast.LENGTH_SHORT, true).show();
                    }
                });
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fba.signOut();
                stopService(new Intent(getApplicationContext(),NotificationService.class));
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("com.package.ACTION_LOGOUT");
                sendBroadcast(broadcastIntent);
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                finish();
            }
        });
        choose_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });
        upload_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
            }
        });
    }

    private void chooseImage()
    {
        Intent intent= new Intent() ;
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"),PICK_IMAGE_REQUEST);

    }

    private void uploadImage() {
        if(filepath != null && resulturi != null)
        {
            final ProgressDialog progressdialog = new ProgressDialog(this);
            progressdialog.setTitle("Uploading Sketch...");
            progressdialog.show();
            if(!isNetworkConnected()){
                progressdialog.dismiss();
                Toasty.warning(Profile.this, "Check Internet Connectivity", Toast.LENGTH_SHORT, true).show();
        }
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            String uid = user.getUid();
            StorageReference ref = storageReference.child("images/users/" + uid + ".jpg");
            ref.putFile(resulturi)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressdialog.dismiss();
                            Uri downloadLink = taskSnapshot.getDownloadUrl();
                            databaseReference.child("userPhoto").setValue(downloadLink.toString());
                            Toasty.success(Profile.this, "Uploaded", Toast.LENGTH_SHORT, true).show();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressdialog.dismiss();
                            Toasty.error(Profile.this, "Upload Failed", Toast.LENGTH_SHORT, true).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                            progressdialog.setMessage("Sketching Portrait : "+(int)progress+"%");
                        }
                    });
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm =
                (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filepath = data.getData();
            CropImage.activity(filepath)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resulturi = result.getUri();
                icon.setImageURI(resulturi);
            }
            else if(resultCode==CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception error = result.getError();
            }

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        }
        return super.onOptionsItemSelected(item);
    }
    }