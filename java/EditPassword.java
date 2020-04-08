package com.example.android.meme;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import es.dmoral.toasty.Toasty;

public class EditPassword extends AppCompatActivity {
    EditText edit_password;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);
        edit_password = findViewById(R.id.edit_password);
        submit = findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edit_password.getText().toString().equals("12345")){
                    startActivity(new Intent(getApplicationContext(),EditNoticeBoard.class));
                    overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                    finish();
                }else if(!isNetworkConnected()){
                    Toasty.warning(EditPassword.this, "Check Internet Connectivity", Toast.LENGTH_SHORT, true).show();
                }else
                    Toasty.error(EditPassword.this, "Wrong Password", Toast.LENGTH_SHORT, true).show();
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
}
