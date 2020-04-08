package com.example.android.meme;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

import static java.lang.Thread.sleep;

public class TabChats extends Fragment {

    private RecyclerView recyclerView;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private EditText searchBox;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_display_users, container, false);

        mAuth = FirebaseAuth.getInstance();

        searchBox = view.findViewById(R.id.search_box);

        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                firebaseSearch(searchBox.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        //recycler view
        recyclerView = view.findViewById(R.id.display_name_recyler_view);
        recyclerView.setLayoutManager(linearLayoutManager);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        mDatabase.keepSynced(true);

        recyclerView.setHasFixedSize(true);
        FirebaseRecyclerAdapter<Users, TabChats.UserViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Users, TabChats.UserViewHolder>
                        (Users.class, R.layout.list_of_users_layout, TabChats.UserViewHolder.class, mDatabase.orderByChild("uName")) {

                    @Override
                    protected void populateViewHolder(TabChats.UserViewHolder viewHolder, Users model, int position) {
                        if (!model.getuAuthID().equals(mAuth.getCurrentUser().getUid())) {

                            String rName = model.getuFirstName()+" "+model.getuLastName();

                            viewHolder.userName.setText(rName);
                            viewHolder.userId.setText(model.getuID().toUpperCase());

                            if(model.getUserPhoto() != null) {
                                Glide.with(getContext()).load(model.getUserPhoto()).into(viewHolder.circleImageView);
                            }

                            final Bundle bundle = new Bundle();
                            bundle.putString("rName", rName);
                            bundle.putString("rId", model.getuAuthID());
                            bundle.putString("rEnrollmentNo",model.getuID());
                            bundle.putString("photo",model.getUserPhoto());

                            Intent notificationServiceForward = new Intent(getContext(),NotificationService.class);
                            Bundle notificationUserData = new Bundle();
                            notificationUserData.putString("rID",model.getuAuthID());

                            notificationServiceForward.putExtras(notificationUserData);

                            getActivity().startService(notificationServiceForward);

                            viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent chat = new Intent(getContext(), MainActivity.class);
                                    chat.putExtras(bundle);
                                    startActivity(chat);
                                }
                            });
                        } else {
                            viewHolder.mView.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
                        }
                    }
                };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
        recyclerView.scrollToPosition(0);


        return view;
    }

    private void firebaseSearch(String userName) {
        Query searchQuery = mDatabase.orderByChild("uFirstName").startAt(userName).endAt(userName + "\uf88f");
        FirebaseRecyclerAdapter<Users, UserViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Users, UserViewHolder>
                        (Users.class,R.layout.list_of_users_layout,UserViewHolder.class,searchQuery){

                    @Override
                    protected void populateViewHolder(UserViewHolder viewHolder, Users model, int position) {
                        if (!model.getuAuthID().equals(mAuth.getCurrentUser().getUid())) {

                            viewHolder.userName.setText(model.getuFirstName() + " " + model.getuLastName());
                            viewHolder.userId.setText(model.getuID());
                            if(model.getUserPhoto() != null)
                            {
                                    Glide.with(getContext()).load(model.getUserPhoto()).into(viewHolder.circleImageView);
                            }

                            final Bundle bundle = new Bundle();
                            bundle.putString("rName", (model.uFirstName + " " + model.getuLastName()));
                            bundle.putString("rId", model.getuAuthID());
                            bundle.putString("rEnrollmentNo",model.uID);
                            bundle.putString("photo",model.getUserPhoto());

                            viewHolder.userName.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent chat = new Intent(getContext(), MainActivity.class);
                                    chat.putExtras(bundle);
                                    startActivity(chat);
                                }
                            });
                        }
                        else {
                            viewHolder.mView.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
                        }
                    }
                };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
        recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount());
        if(searchBox.getText().equals(""))
        {
            firebaseSearch("");
        }
    }

    static public class UserViewHolder extends RecyclerView.ViewHolder {

        View mView;
        TextView userName, userId;
        ImageView circleImageView;

        public UserViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            userName = mView.findViewById(R.id.name_of_user);
            userId = mView.findViewById(R.id.enrollment_no);
            circleImageView = mView.findViewById(R.id.profile_photo);
        }
    }
}
