package com.example.android.meme;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import es.dmoral.toasty.Toasty;

public class About_Us extends AppCompatActivity {
    ImageView logo_image;
    static int counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about__us);
        logo_image = findViewById(R.id.logo_image);
        counter = 0;
        logo_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counter++;
                if(counter == 5){
                    Toasty.success(getApplicationContext(),"Stop clicking like fool,do something cool! :D").show();
                    counter = 0;
                }
            }
        });
    }
}
