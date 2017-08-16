package com.example.mulwa.motivator.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatCallback;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mulwa.motivator.General.DividerItemDecoration;
import com.example.mulwa.motivator.General.Functions;
import com.example.mulwa.motivator.Login;
import com.example.mulwa.motivator.MainActivity;
import com.example.mulwa.motivator.Pojo.Message;
import com.example.mulwa.motivator.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 */
public class Profile extends Fragment  {
    private FirebaseDatabase database;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;
    public String UserName;
    private DatabaseReference mref;
    private RecyclerView mRecyclerView;
    private FloatingActionButton mFab;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        mRecyclerView  = view.findViewById(R.id.recy_my_messages);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),LinearLayoutManager.VERTICAL ));

        mFab = view.findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createMessage();

            }
        });


        checkAuthentication();
        return view;
    }

    private void createMessage() {
       Share share = new Share();
        this.getFragmentManager().beginTransaction()
                .replace(R.id.content_fragment,share)
                .commit();

    }

    @Override
    public void onStart() {
        super.onStart();
        if(mAuth != null){
            mAuth.addAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mAuthListener !=null){
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Profile");

    }

    private void checkAuthentication(){
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if(user != null){
                    UserName = user.getDisplayName();
                }else{
                    startActivity(new Intent(getContext(), MainActivity.class));
                }
                fetchData(UserName);
            }
        };


    }
    private void fetchData(String username){
        mref = database.getReference("Messages").child("Owner").child(username);
        Log.d("Ref","reference"+mref);
        FirebaseRecyclerAdapter<Message,mViewHolder> adapter = new FirebaseRecyclerAdapter<Message, mViewHolder>(
                Message.class,
                R.layout.my_message_custom_layout,
                mViewHolder.class,
                mref
        ) {
            @Override
            protected void populateViewHolder(mViewHolder viewHolder, Message model, int position) {
                viewHolder.tv_message.setText(model.getMessage());

            }
        };
        mRecyclerView.setAdapter(adapter);
    }
    public static class mViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_message;


        public mViewHolder(View itemView) {
            super(itemView);

            tv_message = itemView.findViewById(R.id.ed_my_message);

        }
    }
}
