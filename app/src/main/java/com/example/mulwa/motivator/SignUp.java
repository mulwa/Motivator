package com.example.mulwa.motivator;

import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.mulwa.motivator.General.Functions;
import com.example.mulwa.motivator.Pojo.User;
import com.example.mulwa.motivator.databinding.SignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {
    private SignUpBinding binding;
    private FirebaseAuth mAuth;
    public ProgressDialog mProgressDialog;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase database;
    private FirebaseUser user;
    private DatabaseReference mref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       binding= DataBindingUtil.setContentView(this,R.layout.activity_sign_up);
        setSupportActionBar(binding.toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle("Sign Up");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate()){
                    createAccount(binding.edEmailAddress.getText().toString(),binding.edPassword.getText().toString(),binding.edMobileNo.getText().toString());
                }
            }
        });

    }

    private void createAccount(final String email, String password, final String userMobile){
        showProgressDialog();
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    user = mAuth.getCurrentUser();

                    if(user != null){
//                        for(UserInfo profile : user.getProviderData()){
//                            String UID = profile.getUid();
//                            String name = profile.getDisplayName();
//
//                        }
                        saveAccount(user.getDisplayName(),email,userMobile,user.getUid());
                    }





                }else {
                    hideProgressDialog();
                    showToast("account  not created"+task.getException());


                }

            }
        });;

    }
    private void showToast(String msg){
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
    }
    private void saveAccount(String name, String email, String mobile, String userId){
        mref = database.getReference("UserDetails");
        User user = new User(name,email,mobile);
        mref.child(userId).setValue(user, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                hideProgressDialog();
                if(databaseError == null){
                    clearUi();
                    showToast(" Account Details saved ");
                    startActivity(new Intent(getApplicationContext(),Login.class));
                }else {
                    showToast(databaseError.getMessage().toString());
                }
            }
        });

    }
    private boolean validate(){
        if(TextUtils.isEmpty(binding.edEmailAddress.getText().toString())){
            showToast("Provide your emial Address");
            return false;
        }
        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(binding.edEmailAddress.getText().toString()).matches()){
            Functions.showToast(getApplicationContext(),"Please Enter a Valid Email Address");
            return false;
        }
        if(TextUtils.isEmpty(binding.edPassword.getText().toString())){
            showToast("Set Your Account Password");
            return false;
        }
        if(TextUtils.isEmpty(binding.edMobileNo.getText().toString())){
            showToast("Provide your Contacts");
            return false;
        }
        return true;
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.creatingAccount));
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
        binding.edEmailAddress.setText("");
        binding.edMobileNo.setText("");
        binding.edPassword.setText("");
    }
}
