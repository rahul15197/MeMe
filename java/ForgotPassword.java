package com.example.android.meme;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import es.dmoral.toasty.Toasty;

public class ForgotPassword extends AppCompatActivity {

    private Button forgot_button;
    private EditText forgot_email;
    private FirebaseAuth fba;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        forgot_button = findViewById(R.id.forgot_button);
        forgot_email = findViewById(R.id.forgot_email);
        fba = FirebaseAuth.getInstance();
        forgot_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = forgot_email.getText().toString().trim();
                fba.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toasty.success(ForgotPassword.this, "Password Reset Email sent", Toast.LENGTH_SHORT, true).show();
                            overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                            finish();
                        }
                        else if(!isNetworkConnected()){
                            Toasty.warning(ForgotPassword.this, "Check Internet Connectivity", Toast.LENGTH_SHORT, true).show();
                        }
                        else
                            Toasty.error(ForgotPassword.this, "Something went wrong", Toast.LENGTH_SHORT, true).show();                   }
                });
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
