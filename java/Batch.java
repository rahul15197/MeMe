package com.example.android.meme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Batch extends AppCompatActivity {
    ListView batch_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().setTitle("Batch Notices");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_batch);
        batch_list = findViewById(R.id.list);
        String[] values = new String[] { "2014-2018",
                "2015-2019",
                "2016-2020",
                "2017-2021"
        };
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,android.R.id.text1,values);
        batch_list.setAdapter(adapter);
        batch_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int position = i;
                String  itemValue = (String)batch_list.getItemAtPosition(position);
                if(itemValue.equals("2014-2018")){
                    startActivity(new Intent(getApplicationContext(),FourthYear.class));
                    overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                }
                else if(itemValue.equals("2015-2019")){
                    startActivity(new Intent(getApplicationContext(),ThirdYear.class));
                    overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                }
                else if(itemValue.equals("2016-2020")){
                    startActivity(new Intent(getApplicationContext(),SecondYear.class));
                    overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                }
                else if(itemValue.equals("2017-2021")){
                    startActivity(new Intent(getApplicationContext(),FirstYear.class));
                    overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                }
            }
        });
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
