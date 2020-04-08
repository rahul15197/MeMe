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

public class Department extends AppCompatActivity {
    ListView dept_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Department Notices");
        setContentView(R.layout.activity_department);
        dept_list = findViewById(R.id.list);
        String[] values = new String[] { "Computer Science (CS)",
                "Information Technology (IT)",
                "Electrical Engineering (EE)",
                "Electronics and Communication (EC)",
                "Electronics and Instrumentation (EI)",
                "Fire Technology (FT)",
                "Mechanical Engineering (ME)",
                "Automobile Engineering (AE)",
                "Civil Engineering (CE)",
                "Nano Technology",
                "MCA",
                "Phd",
                "BCA",
                "B.Sc.(Computer Science)",
                "B.Com.",
                "MBA",
                "Bachelor of Business (BB)"
        };


        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,android.R.id.text1,values);
        dept_list.setAdapter(adapter);
        dept_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int position = i;
                String  itemValue = (String)dept_list.getItemAtPosition(position);
                if(itemValue.equals("Computer Science (CS)")){
                    startActivity(new Intent(getApplicationContext(),CS.class));
                    overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                }
                else if(itemValue.equals("Information Technology (IT)")){
                    startActivity(new Intent(getApplicationContext(),IT.class));
                    overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                }
                else if(itemValue.equals("Electrical Engineering (EE)")){
                    startActivity(new Intent(getApplicationContext(),EE.class));
                    overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                }
                else if(itemValue.equals("Electronics and Communication (EC)")){
                    startActivity(new Intent(getApplicationContext(),EC.class));
                    overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                }
                else if(itemValue.equals("Electronics and Instrumentation (EI)")){
                    startActivity(new Intent(getApplicationContext(),EI.class));
                    overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                }
                else if(itemValue.equals("Fire Technology (FT)")){
                    startActivity(new Intent(getApplicationContext(),FT.class));
                    overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                }
                else if(itemValue.equals("Mechanical Engineering (ME)")){
                    startActivity(new Intent(getApplicationContext(),ME.class));
                    overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                }
                else if(itemValue.equals("Automobile Engineering (AE)")){
                    startActivity(new Intent(getApplicationContext(),AE.class));
                    overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                }
                else if(itemValue.equals("Civil Engineering (CE)")){
                    startActivity(new Intent(getApplicationContext(),CE.class));
                    overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                }
                else if(itemValue.equals("Nano Technology")){
                    startActivity(new Intent(getApplicationContext(),Nano.class));
                    overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                }
                else if(itemValue.equals("MCA")){
                    startActivity(new Intent(getApplicationContext(),MCA.class));
                    overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                }
                else if(itemValue.equals("Phd")){
                    startActivity(new Intent(getApplicationContext(),Phd.class));
                    overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                }
                else if(itemValue.equals("BCA")){
                    startActivity(new Intent(getApplicationContext(),BCA.class));
                    overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                }
                else if(itemValue.equals("B.Sc.(Computer Science)")){
                    startActivity(new Intent(getApplicationContext(),Bsc.class));
                    overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                }
                else if(itemValue.equals("B.Com.")){
                    startActivity(new Intent(getApplicationContext(),Bcom.class));
                    overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                }
                else if(itemValue.equals("MBA")){
                    startActivity(new Intent(getApplicationContext(),MBA.class));
                    overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                }
                else if(itemValue.equals("Bachelor of Business (BB)")){
                    startActivity(new Intent(getApplicationContext(),BB.class));
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
        }
        return super.onOptionsItemSelected(item);
    }
}
