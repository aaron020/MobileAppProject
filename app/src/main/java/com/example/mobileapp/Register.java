package com.example.mobileapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {
    EditText editTextRegisterUsername;
    EditText editTextRegisterEmailAddress;
    EditText editTextRegisterPassword;
    EditText editTextRegisterConfirmPassword;
    ProgressBar progressBarRegister;
    Button buttonRegister;
    FirebaseAuth fAuth;
    private final int PASS_LENGTH = 6;

    private String username;
    private String email;
    private String password;
    private String passwordConfirmed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        createElements();
        buttonRegister.setEnabled(false);
        verifyFieldsListener();
        clickRegisterButton();





    }


    private void verifyFieldsListener(){
        editTextRegisterConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                editTextRegisterConfirmPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_baseline_lock_open_24,0,0,0);
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                if( !(editTextRegisterPassword.getText().toString().trim().equals(editTextRegisterConfirmPassword.getText().toString().trim())) ){
                    //Set the error icon visible
                    editTextRegisterConfirmPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_baseline_lock_open_24,0,R.drawable.ic_baseline_error_24,0);
                    buttonRegister.setEnabled(false);
                }else{
                    editTextRegisterConfirmPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_baseline_lock_open_24,0,R.drawable.ic_baseline_check_24,0);
                    buttonRegister.setEnabled(true);
                }
                if(editTextRegisterConfirmPassword.getText().toString().isEmpty()){
                    editTextRegisterConfirmPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_baseline_lock_open_24,0,0,0);
                }
            }
        });

        editTextRegisterPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if(editTextRegisterPassword.getText().toString().trim().length() < PASS_LENGTH){
                    editTextRegisterPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_baseline_lock_24,0,R.drawable.ic_baseline_error_24,0);
                }else{
                    editTextRegisterPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_baseline_lock_24,0,R.drawable.ic_baseline_check_24,0);
                }
                if(editTextRegisterPassword.getText().toString().isEmpty()){
                    editTextRegisterPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_baseline_lock_24,0,0,0);
                }

                if( !(editTextRegisterPassword.getText().toString().trim().equals(editTextRegisterConfirmPassword.getText().toString().trim())) ){
                    //Set the error icon visible
                    editTextRegisterConfirmPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_baseline_lock_open_24,0,R.drawable.ic_baseline_error_24,0);
                    buttonRegister.setEnabled(false);
                }else{
                    editTextRegisterConfirmPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_baseline_lock_open_24,0,R.drawable.ic_baseline_check_24,0);
                    buttonRegister.setEnabled(true);
                }
                if(editTextRegisterConfirmPassword.getText().toString().isEmpty()){
                    editTextRegisterConfirmPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_baseline_lock_open_24,0,0,0);
                }
            }
        });
    }


    private void clickRegisterButton(){
        //When the register button is clicked
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBarRegister.setVisibility(View.VISIBLE);
                username = editTextRegisterUsername.getText().toString().trim();
                email = editTextRegisterEmailAddress.getText().toString().trim();
                password = editTextRegisterPassword.getText().toString().trim();
                passwordConfirmed = editTextRegisterConfirmPassword.getText().toString().trim();
                registerAccount(email,password, username);

            }
        });
    }

    private void registerAccount(String email, String password, String username){
        if(email.isEmpty() || password.isEmpty() || username.isEmpty()){
            Toast.makeText(Register.this,"Missing Field!", Toast.LENGTH_SHORT).show();
            progressBarRegister.setVisibility(View.INVISIBLE);
            return;
        }
        fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(Register.this,"Hi " + username + ", your account has been created :)", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Register.this, MainActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(Register.this,"Error " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    progressBarRegister.setVisibility(View.INVISIBLE);
                }
            }
        });

    }



    private void createElements(){
        editTextRegisterUsername = findViewById(R.id.editTextRegisterUsername);
        editTextRegisterEmailAddress = findViewById(R.id.editTextRegisterEmailAddress);
        editTextRegisterPassword = findViewById(R.id.editTextRegisterPassword);
        editTextRegisterPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_baseline_lock_24,0,0,0);
        editTextRegisterConfirmPassword = findViewById(R.id.editTextRegisterPasswordConfirm);
        //Add drawables - error is invisible
        editTextRegisterConfirmPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_baseline_lock_open_24,0,0,0);

        progressBarRegister = findViewById(R.id.progressBarRegister);
        buttonRegister = findViewById(R.id.buttonRegister);
        fAuth = FirebaseAuth.getInstance();
    }

    //When the Sign In text is clicked on the login page this method is called
    public void signInAccountPage(View view){
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }
}