package com.example.android.meme;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class RegisterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private EditText fname;
    private EditText lname;
    private Spinner clg_spinner;
    private EditText id;
    private EditText mEmail;
    private EditText mPassword,mConfirmPass,batch;
    private RadioButton radio_id;
    private RadioGroup radioGroup;
    private Button register;
    private String userType;
    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private FloatingActionButton fab;
    private TextInputLayout fname_layout,lname_layout,email_layout,pass_layout,confirm_pass_layout,id_layout,batch_layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);
        fname = findViewById(R.id.fname);
        id = findViewById(R.id.id);
        lname = findViewById(R.id.lname);
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        mConfirmPass = findViewById(R.id.confirmpassword);
        batch = findViewById(R.id.batch);
        progressBar = findViewById(R.id.progressBarRegister);
        fname_layout = findViewById(R.id.fname_layout);
        lname_layout = findViewById(R.id.lname_layout);
        email_layout = findViewById(R.id.email_layout);
        pass_layout = findViewById(R.id.password_layout);
        fab = findViewById(R.id.floating);
        confirm_pass_layout = findViewById(R.id.confirmpassword_layout);
        id_layout = findViewById(R.id.id_layout);
        batch_layout = findViewById(R.id.batch_layout);
        id.setHintTextColor(getResources().getColor(R.color.white));
        radioGroup = findViewById(R.id.radiogroup);
        id.setVisibility(View.GONE);
        clg_spinner = findViewById(R.id.clg_spinner);
        clg_spinner.setOnItemSelectedListener(this);
        register = (Button)findViewById(R.id.register_button);
        List<String> categories = new ArrayList<String>();
        categories.add("University");
        categories.add("MITM");
        categories.add("MIST");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,R.layout.spinner_item_white, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        clg_spinner.setAdapter(dataAdapter);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int selected_radio) {
                radio_id = findViewById(selected_radio);
                if(radio_id.getId()==R.id.radio_student) {
                    id.setVisibility(View.VISIBLE);
                    id.setHint("Enrollment Id");
                    userType = "Student";
                }
                else{
                    id.setVisibility(View.VISIBLE);
                    id.setHint("Faculty Id");
                    userType="Faculty";
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                        submitForm();
                    }
                });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                finish();
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
            }
        });
    }

    private void submitForm() {
        if (!validateFName()) {
            return;
        }
        if (!validateLName()) {
            return;
        }
        if (!validateEmail()) {
            return;
        }
        if (!validatePassword()) {
            return;
        }
        if (!validateConfirmPassword()) {
            return;
        }
        if (!validateRadio()) {
            return;
        }
        if (!validateDropDown()) {
            return;
        }
        if (!validateBatch()) {
            return;
        }
        register.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(mEmail.getText().toString(), mPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    String uid = mAuth.getCurrentUser().getUid();
                    mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
                    mDatabaseReference.child("uAuthID").setValue(mAuth.getCurrentUser().getUid());
                    mDatabaseReference.child("uFirstName").setValue(fname.getText().toString());
                    mDatabaseReference.child("uLastName").setValue(lname.getText().toString());
                    mDatabaseReference.child("uID").setValue(id.getText().toString());
                    mDatabaseReference.child("uUserType").setValue(userType);
                    mDatabaseReference.child("uEmail").setValue(mEmail.getText().toString());
                    mDatabaseReference.child("uCollege").setValue(clg_spinner.getSelectedItem().toString());
                    mDatabaseReference.child("uBatch").setValue(batch.getText().toString());
                  //  mDatabaseReference.child("uProfilePhotoURL").setValue("0");
                    Toasty.success(RegisterActivity.this, "Yay! Registration Done", Toast.LENGTH_SHORT, true).show();
                    mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                Toasty.success(RegisterActivity.this, "Check Registered email for verification mail", Toast.LENGTH_SHORT, true).show();
                                finish();
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            }
                        }
                    });
                }
                else if(!isNetworkConnected()){
                    Toasty.warning(RegisterActivity.this, "Check Internet Connectivity", Toast.LENGTH_SHORT, true).show();
                    register.setVisibility(View.VISIBLE);
                }
                else {
                    FirebaseAuthException e = (FirebaseAuthException )task.getException();
                    Toasty.error(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT, true).show();
                    register.setVisibility(View.VISIBLE);
                }
            }
        });
        mAuth.signOut();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
    }

    private boolean validateBatch() {
        if (batch.getText().toString().isEmpty()|batch.getText().toString().length()>4){
            batch_layout.setError("Enter a valid Batch year");
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            return false;
        }else
            batch_layout.setErrorEnabled(false);
        return true;
    }

    private boolean validateDropDown() {
        if(clg_spinner.getSelectedItem()==null){
            Toast.makeText(this, "Please select your college", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean validateRadio() {
        if(!(radio_id.getId()==R.id.radio_student || radio_id.getId()==R.id.radio_faculty)){
            Toast.makeText(this, "Please select one of the radio button", Toast.LENGTH_SHORT).show();
            return false;
        }
            return true;
    }

    private boolean validateConfirmPassword() {
        if(!(mPassword.getText().toString().equals(mConfirmPass.getText().toString()))){
            confirm_pass_layout.setError("Passwords do not match!");
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            return false;
        }else
            confirm_pass_layout.setErrorEnabled(false);
        return true;
    }

    private boolean validatePassword() {
        if(mPassword.getText().toString().isEmpty()||mPassword.getText().toString().length()<8){
            pass_layout.setError(getString(R.string.password_error));
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            return false;
        }else
            pass_layout.setErrorEnabled(false);
        return true;
    }

    private boolean validateEmail() {
        String mail = mEmail.getText().toString().trim();
        if(mail.isEmpty()|!(mail.toString().contains("@"))){
            email_layout.setError("Please enter a valid Email Address");
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            return false;
        }else
            email_layout.setErrorEnabled(false);
        return true;
    }

    private boolean validateLName() {
        String last = lname.getText().toString().trim();
        if(last.isEmpty()|!(last.matches("[a-zA-Z]+"))){
            lname_layout.setError("Last Name is required and must contain only alphabets");
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            return false;
        }else
            lname_layout.setErrorEnabled(false);
        return true;
    }

    private boolean validateFName() {
                String first = fname.getText().toString().trim();
            if(first.isEmpty()|!(first.matches("[a-zA-Z]+"))){
                fname_layout.setError("First Name is required and must contain only alphabets");
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                return false;
            }else
                fname_layout.setErrorEnabled(false);
            return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String item = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    private long mBackPressed;

    @Override
    public void onBackPressed()
    {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis())
        {
            super.onBackPressed();
            overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
            return;
        }
        else { Snackbar snackbar = Snackbar.make(findViewById(R.id.linearlayout_register),"Press back again to exit app",Snackbar.LENGTH_SHORT);
            snackbar.show();}

        mBackPressed = System.currentTimeMillis();
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm =
                (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }
}
