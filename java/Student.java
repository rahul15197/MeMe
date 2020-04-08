package com.example.android.meme;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import es.dmoral.toasty.Toasty;

public class Student extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private DatabaseReference mDatabaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private static String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().setTitle("Student Notices");
        setContentView(R.layout.activity_student);
        mRecyclerView = findViewById(R.id.post_list);
        mRecyclerView.setHasFixedSize(true);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        uid = user.getUid();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Notices");
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
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

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Post,PostViewHolder> FBRA = new FirebaseRecyclerAdapter<Post, PostViewHolder>(
                Post.class,
                R.layout.post_design,
                PostViewHolder.class,
                mDatabaseReference
        )
        {
            @Override
            protected void populateViewHolder(PostViewHolder viewHolder, Post model, int position) {
                if(viewHolder.Type(model.getType()).equals("Student Notice")) {
                    viewHolder.mView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    viewHolder.setTitle(model.getTitle());
                    viewHolder.setDescription(model.getDescription());
                    viewHolder.setPhotoURL(getApplicationContext(), model.getPhotoURL());
                    viewHolder.setPostedBy(model.getPosted_by());
                    viewHolder.setDate(model.getDate());
                    viewHolder.DeletePost(getApplicationContext(), model.getUID(), model.getPost_id());
                }
                else{
                    viewHolder.mView.setLayoutParams(new LinearLayout.LayoutParams(0,0));
                }
            }
        };
        mRecyclerView.setAdapter(FBRA);
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder{
        View mView;

        public PostViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setTitle(String title){
            TextView post_title = (TextView)itemView.findViewById(R.id.post_title);
            post_title.setText(title);
        }

        public void setDescription(String description){
            TextView post_desc = (TextView)itemView.findViewById(R.id.post_description);
            post_desc.setText(description);
        }

        public void setPhotoURL(Context ctx,String PhotoURL){
            PhotoView post_image = (PhotoView)itemView.findViewById(R.id.post_image);
            Glide.with(ctx).load(PhotoURL).into(post_image);
        }
        public void DeletePost(final Context ctx, final String UID, final String post_id){
            TextView post_delete = (TextView)itemView.findViewById(R.id.post_delete);
            post_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(uid.equals(UID)){
                        Intent intent = new Intent(view.getContext(),DeletePostClass.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("key",post_id);
                        ctx.startActivity(intent);
                    }else
                        Toasty.error(ctx, "Not authorized to delete the post", Toast.LENGTH_SHORT, true).show();
                }
            });
        }
        public void setPostedBy(String posted_by){
            TextView posted_by_admin = (TextView)itemView.findViewById(R.id.posted_by_admin);
            posted_by_admin.setText("Posted by " + posted_by);
        }

        public void setDate(String date){
            TextView post_date = (TextView)itemView.findViewById(R.id.post_date);
            post_date.setText("Posted on " + date);
        }

        public String Type(String Type){
            return Type;
        }
        public String Dept_Type(String Dept_Type){
            return Dept_Type;
        }
    }
}


