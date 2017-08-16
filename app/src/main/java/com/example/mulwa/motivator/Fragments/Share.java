package com.example.mulwa.motivator.Fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mulwa.motivator.General.Functions;
import com.example.mulwa.motivator.Login;
import com.example.mulwa.motivator.MainActivity;
import com.example.mulwa.motivator.Pojo.Message;
import com.example.mulwa.motivator.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class Share extends Fragment {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListerner;
    private TextView mMessage;
    private Button mButtonSend;
    private DatabaseReference mref;
    private FirebaseDatabase database;
    private String userId,userName;
    private String userPhoto;
    public ProgressDialog mProgressDialog;
    private DatabaseReference userNodeRef;


    public Share() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View  view = inflater.inflate(R.layout.fragment_share, container, false);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        checkAuthentication();



        initializeUi(view);
        mButtonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateInput()){
                    saveMessage();
                }

            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListerner);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mAuthListerner != null){
            mAuth.removeAuthStateListener(mAuthListerner);
        }
    }

    private void  initializeUi(View v){
        mMessage = v.findViewById(R.id.tvMessage);
        mButtonSend = v.findViewById(R.id.btnShare);

    }
    private void showToast(String msg){
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }
    private boolean validateInput(){
        if(TextUtils.isEmpty(mMessage.getText().toString().trim())){
            showToast("Please Provide Your Motivation Quote");
            return false;
        }
        return true;
    }
    private void checkAuthentication(){
        mAuthListerner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if(firebaseUser != null){
                    userName = firebaseUser.getDisplayName();
                    userId = firebaseUser.getUid();
                    userPhoto = String.valueOf(firebaseUser.getPhotoUrl());

                }else {

                    startActivity(new Intent(getContext(), Login.class));
                }

            }
        };
    }
    private void saveMessage(){
        showProgressDialog();
        mref = database.getReference("Messages");
        userNodeRef = database.getReference("Messages").child("Owner");
        final String key = mref.push().getKey();
        final Message message = new Message(mMessage.getText().toString(),userId,"14-08-2017",userName,userPhoto);
        mref.child(key).setValue(message, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                hideProgressDialog();
                if(databaseError == null){
                    clearUi();
                    userNodeRef.child(userName).child(key).setValue(message);
                    Functions.showToast(getContext(),"Message Saved Successfully");
                    startActivity(new Intent(getContext(), MainActivity.class));

                }else {
                    Functions.showToast(getContext(),"Note saved"+databaseError);
                }
            }
        });




    }
    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getContext());
            mProgressDialog.setMessage("Saving Message...   ");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }
    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
    private void clearUi(){
        mMessage.setText("");
    }


}
