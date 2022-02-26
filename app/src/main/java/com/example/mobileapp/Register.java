package com.example.mobileapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

public class Register extends AppCompatActivity {
    EditText editTextRegisterUsername;
    EditText editTextRegisterEmailAddress;
    EditText editTextRegisterPassword;
    EditText editTextRegisterConfirmPassword;
    ProgressBar progressBarRegister;
    Button buttonRegister;

    private String username;
    private String email;
    private String password;
    private String passwordConfirmed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        createElements();



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

                }
                if(editTextRegisterConfirmPassword.getText().toString().isEmpty()){
                    editTextRegisterConfirmPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_baseline_lock_open_24,0,0,0);
                }
            }
        });

        //When the register button is clicked
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBarRegister.setVisibility(View.VISIBLE);
                username = editTextRegisterUsername.getText().toString().trim();
                email = editTextRegisterEmailAddress.getText().toString().trim();
                password = editTextRegisterPassword.getText().toString().trim();
                passwordConfirmed = editTextRegisterConfirmPassword.getText().toString().trim();

                System.out.println(username + "  " + email + "  " + password + "  " + passwordConfirmed);

                if( !(password.equals(passwordConfirmed)) ){
                    //Set the error icon visible
                    editTextRegisterConfirmPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_baseline_lock_open_24,0,R.drawable.ic_baseline_error_24,0);

                }

            }
        });
    }



    private void createElements(){
        editTextRegisterUsername = findViewById(R.id.editTextRegisterUsername);
        editTextRegisterEmailAddress = findViewById(R.id.editTextRegisterEmailAddress);
        editTextRegisterPassword = findViewById(R.id.editTextRegisterPassword);
        editTextRegisterConfirmPassword = findViewById(R.id.editTextRegisterPasswordConfirm);
        //Set the error symbol invisible
        editTextRegisterConfirmPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_baseline_lock_open_24,0,0,0);

        progressBarRegister = findViewById(R.id.progressBarRegister);
        buttonRegister = findViewById(R.id.buttonRegister);
    }

    //When the Sign In text is clicked on the login page this method is called
    public void signInAccountPage(View view){
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }
}