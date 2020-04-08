package com.example.android.meme;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import es.dmoral.toasty.Toasty;

public class DeletePostClass extends AppCompatActivity {
    EditText post_delete_edittext;
    Button post_delete_button;
    DatabaseReference mDatabaseReference;
    private String post_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_post_class);
        post_delete_button = findViewById(R.id.post_delete_button);
        post_delete_edittext = findViewById(R.id.post_delete_edittext);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Notices");
        post_delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(post_delete_edittext.getText().toString().trim().equals("12345")){
                    post_id = getIntent().getStringExtra("key");
                    Toasty.success(DeletePostClass.this, "Post Deleted", Toast.LENGTH_SHORT, true).show();
                    mDatabaseReference.child(post_id).removeValue();
                    finish();
                }
            }
        });
    }
}
