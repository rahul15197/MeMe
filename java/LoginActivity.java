package com.example.android.meme;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.RemoteMessage;

import es.dmoral.toasty.Toasty;

public class LoginActivity extends AppCompatActivity {
    private EditText email,password;
    private Button signin;
    private TextView toregister,forgot_password;
    private TextInputLayout email_layout,password_layout;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        email = findViewById(R.id.email_text);
        password = findViewById(R.id.password_text);
        email_layout = findViewById(R.id.email_layout);
        password_layout = findViewById(R.id.password_layout);
        signin = findViewById(R.id.sign_in);
        toregister = findViewById(R.id.to_register);
        forgot_password = findViewById(R.id.forgot_password);
        mAuth = FirebaseAuth.getInstance();
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        if(mAuth.getCurrentUser()!=null){
            startActivity(new Intent(getApplicationContext(),NoticeBoard.class));
            overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
            finish();
        }

        email.addTextChangedListener(new MyTextWatcher(email));
        password.addTextChangedListener(new MyTextWatcher(password));

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitForm();
            }
        });

        toregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toregisteractivity = new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(toregisteractivity);
                finish();
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
            }
        });

        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    startActivity(new Intent(getApplicationContext(),ForgotPassword.class));
                    overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
            }
        });
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm =
                (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

        private void submitForm(){
            if (!validateEmail()) {
                return;
            }

            if (!validatePassword()) {
                return;
            }
            progressBar.setVisibility(View.VISIBLE);
            mAuth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progressBar.setVisibility(View.GONE);
                    if(task.isSuccessful()){
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if(user.isEmailVerified()){
                            startActivity(new Intent(getApplicationContext(),NoticeBoard.class));
                            overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                            finish();
                        }
                        else {
                            Toasty.info(LoginActivity.this, "Email not verified", Toast.LENGTH_SHORT, true).show();
                            FirebaseAuth.getInstance().signOut();
                        }
                    }
                    else if(!isNetworkConnected()){
                        Toasty.warning(LoginActivity.this, "Check Internet Connectivity", Toast.LENGTH_SHORT, true).show();
                    }
                    else{
                        Toasty.error(LoginActivity.this, "Wrong Email or Password", Toast.LENGTH_SHORT, true).show();
                    }
                }
            });
        }

    private boolean validatePassword() {
            if(password.getText().toString().isEmpty()||password.getText().toString().length()<8){
                password_layout.setError(getString(R.string.password_error));
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                return false;
            }else
                password_layout.setErrorEnabled(false);
            return true;
    }

    private boolean validateEmail() {
        if(email.getText().toString().isEmpty()||!(email.getText().toString().contains("@"))){
            email_layout.setError(getString(R.string.email_error));
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            return false;
        }else
            email_layout.setErrorEnabled(false);
        return true;
    }


    private class MyTextWatcher implements TextWatcher {
        private View view;

        public MyTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            switch (view.getId()){
                case R.id.email:
                    validateEmail();
                    break;
                case R.id.password:
                    validatePassword();
                    break;
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
            switch (view.getId()){
                case R.id.email:
                    validateEmail();
                    break;
                case R.id.password:
                    validatePassword();
                    break;
            }

        }
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
        else { Snackbar snackbar = Snackbar.make(findViewById(R.id.linearlayout_login),"Press back again to exit app",Snackbar.LENGTH_SHORT);
            snackbar.show(); }

        mBackPressed = System.currentTimeMillis();
    }
}