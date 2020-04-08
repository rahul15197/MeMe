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
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class EditNoticeBoard extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private Spinner type_spinner,batch_year_spinner,dept_type_spinner;
    private Button post_button,upload_button;
    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mAuth;
    private EditText title,description,posted_by;
    private ImageView photo;
    private Uri filepath,resulturi,downloadLink;
    FirebaseStorage storage;
    StorageReference storageReference;
    private Calendar calendar;
    private SimpleDateFormat simpledateformat;
    String Date;

    String pushKey = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_edit_notice_board);
        post_button = findViewById(R.id.post_button);
        type_spinner = findViewById(R.id.type);
        batch_year_spinner = findViewById(R.id.batch_year);
        dept_type_spinner = findViewById(R.id.dept_type);
        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
        photo = findViewById(R.id.photo);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        posted_by = findViewById(R.id.posted_by);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Notices").push();
        pushKey = mDatabaseReference.getKey();

        List<String> type = new ArrayList<String>();
        type.add("Important Notice");
        type.add("Department Notice");
        type.add("Event Notice");
        type.add("Placement Notice");
        type.add("Batch Notice");
        type.add("Student Notice");
        type.add("Faculty Notice");

        ArrayAdapter<String> type_Adapter = new ArrayAdapter<String>(this,R.layout.spinner_item_white,type);
        type_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type_spinner.setAdapter(type_Adapter);
        type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(type_spinner.getSelectedItem().toString().equals("Department Notice")){
                    dept_type_spinner.setVisibility(View.VISIBLE);
                    batch_year_spinner.setVisibility(View.GONE);
                }
                else if(type_spinner.getSelectedItem().toString().equals("Batch Notice")){
                    batch_year_spinner.setVisibility(View.VISIBLE);
                    dept_type_spinner.setVisibility(View.GONE);
                }
                else{
                    batch_year_spinner.setVisibility(View.GONE);
                    dept_type_spinner.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        final List<String> dept_type = new ArrayList<String>();
        dept_type.add("Computer Science (CS)");
        dept_type.add("Information Technology (IT)");
        dept_type.add("Electrical Engineering (EE)");
        dept_type.add("Electronics and Communication (EC)");
        dept_type.add("Electronics and Instrumentation (EI)");
        dept_type.add("Fire Technology (FT)");
        dept_type.add("Mechanical Engineering (ME)");
        dept_type.add("Automobile Engineering (AE)");
        dept_type.add("Civil Engineering (CE)");
        dept_type.add("Nano Technology");
        dept_type.add("MCA");
        dept_type.add("Phd");
        dept_type.add("BCA");
        dept_type.add("B.Sc.(Computer Science)");
        dept_type.add("B.Com.");
        dept_type.add("MBA");
        dept_type.add("Bachelor of Business (BB)");

        ArrayAdapter<String> dept_type_Adapter = new ArrayAdapter<String>(this,R.layout.spinner_item_white, dept_type);
        dept_type_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dept_type_spinner.setAdapter(dept_type_Adapter);


        List<String> batch_year_type = new ArrayList<String>();
        batch_year_type.add("2014-2018");
        batch_year_type.add("2015-2019");
        batch_year_type.add("2016-2020");
        batch_year_type.add("2017-2021");

        ArrayAdapter<String> batch_year_Adapter = new ArrayAdapter<String>(this,R.layout.spinner_item_white, batch_year_type);
        batch_year_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        batch_year_spinner.setAdapter(batch_year_Adapter);

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        post_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String uid = mAuth.getCurrentUser().getUid();
                calendar = Calendar.getInstance();
                simpledateformat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                Date = simpledateformat.format(calendar.getTime());
                mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot!=null)
                            if(downloadLink == null || downloadLink.equals(Uri.EMPTY)) {
                                Toasty.error(EditNoticeBoard.this, "Please select an image first", Toast.LENGTH_SHORT, true).show();
                            } else{
                                mDatabaseReference.child("PhotoURL").setValue(downloadLink.toString());
                                mDatabaseReference.child("title").setValue(title.getText().toString());
                                mDatabaseReference.child("UID").setValue(uid);
                                mDatabaseReference.child("description").setValue(description.getText().toString());
                                mDatabaseReference.child("Type").setValue(type_spinner.getSelectedItem().toString());
                                mDatabaseReference.child("post_id").setValue(getPushKey());
                                mDatabaseReference.child("posted_by").setValue(posted_by.getText().toString());
                                mDatabaseReference.child("date").setValue(Date);
                                if(dept_type_spinner.getVisibility()==View.VISIBLE) {
                                    mDatabaseReference.child("Dept_Type").setValue(dept_type_spinner.getSelectedItem().toString());
                                }
                                else if(batch_year_spinner.getVisibility()==View.VISIBLE) {
                                    mDatabaseReference.child("Batch_Year_Type").setValue(batch_year_spinner.getSelectedItem().toString());
                                }
                                Toasty.success(EditNoticeBoard.this, "Notice Successfully Posted", Toast.LENGTH_SHORT, true).show();
                            }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                });

            }
        });
    }

    public String getPushKey()
    {
        return pushKey;
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
            progressdialog.setTitle("Uploading");
            progressdialog.show();
            if(!isNetworkConnected()){
                progressdialog.dismiss();
                Toasty.warning(EditNoticeBoard.this, "Check Internet Connectivity", Toast.LENGTH_SHORT, true).show();
            }
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            String uid = user.getUid();
            StorageReference ref = storageReference.child("notices/" + filepath.getLastPathSegment());
            ref.putFile(resulturi)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressdialog.dismiss();
                            downloadLink = taskSnapshot.getDownloadUrl();
                            mDatabaseReference.child("PhotoURL").setValue(downloadLink.toString());
                            Toasty.success(getApplicationContext(), "Uploaded", Toast.LENGTH_SHORT, true).show();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressdialog.dismiss();
                            Toasty.error(getApplicationContext(), "Upload Failed", Toast.LENGTH_SHORT, true).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                            progressdialog.setMessage("Uploading Notice image : "+(int)progress+"%");
                        }
                    });
        }else
            Toasty.error(EditNoticeBoard.this, "Please select an image first", Toast.LENGTH_SHORT, true).show();
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
                photo.setImageURI(resulturi);
                uploadImage();
            }
            else if(resultCode==CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception error = result.getError();
                Toasty.error(getApplicationContext(), "Error : " + error.toString(), Toast.LENGTH_SHORT, true).show();
            }

        }
    }
}
