package com.example.houssem.roadsecurity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private EditText emailField;
    private EditText passField;
    private CardView loginButton;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(SharedPrefManager.getInstance(LoginActivity.this).isLoggedIn()){
            startActivity(new Intent(LoginActivity.this,ProfileActivity.class));
        }
        setContentView(R.layout.activity_login);
        emailField=(EditText)findViewById(R.id.email);
        passField=(EditText)findViewById(R.id.pass);
        loginButton=(CardView)findViewById(R.id.login);
        mAuth=FirebaseAuth.getInstance();
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLogin();
            }
        });
    }

    protected void onResume(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if(SharedPrefManager.getInstance(LoginActivity.this).isLoggedIn()){
            startActivity(new Intent(LoginActivity.this,ProfileActivity.class));
            finish();
        }
    }

    private void startLogin(){
        final String email=emailField.getText().toString();
        final String password=passField.getText().toString();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Connecting...");
        if(TextUtils.isEmpty(email)||TextUtils.isEmpty(password)){
            if(TextUtils.isEmpty(email)){
                emailField.setError("Email field is Empty");
            }
            if(TextUtils.isEmpty(password)){
                passField.setError("password field is Empty");
            }
        }
        else{
            progressDialog.show();
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                        SharedPrefManager.getInstance(LoginActivity.this).userLogin(email);
                        startActivity(new Intent(LoginActivity.this,ProfileActivity.class));
                        finish();
                    }
                    else {
                        // If sign in fails, display a message to the user.
                        progressDialog.dismiss();
                        passField.setError("Invalid Email or Password");
                        emailField.setError("Invalid Email or Password");
                    }
                }
            });
        }
    }
}
