package com.example.mobileapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    EditText editTextLoginEmailAddress;
    EditText editTextLoginPassword;
    Button buttonLogin;
    ProgressBar progressBarLogin;
    FirebaseAuth fAuth;
    private String email;
    private String password;
    private boolean emailFilled = false;
    private boolean passwordFilled = false;
    private final int PASS_LENGTH = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Creates all the global variables
        createElements();
        //Listener to check if the login button has been pressed
        clickLoginListener();
        //Listener to check if the email and password have been entered
        //Only if they have both been entered will the Login button be enabled
        verifyFieldsListener();

        //Checks if the user is already logged in and if they are you go straight to the
        //home page
        if (FirebaseAuth.getInstance().getCurrentUser() != null){
            Intent intent = new Intent(MainActivity.this, Home.class);
            startActivity(intent);
            finish();
        }


    }
    /*
    This function first checks if the username and password are not empty, it then uses
    firebase to check the creds, if they are correct it logs you into the home page
     */
    private void login(String email, String password){
        if(email.isEmpty() || password.isEmpty()){
            Toast.makeText(MainActivity.this,"Missing Field!", Toast.LENGTH_SHORT).show();
            return;
        }
        fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(MainActivity.this,"Signed In", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, Home.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(MainActivity.this,"Error " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    progressBarLogin.setVisibility(View.INVISIBLE);
                }
            }
        });
    }



    /*
    A listener to check if the email and password have been entered and if they have the
    login button is enabled
     */
    private void verifyFieldsListener(){
        editTextLoginEmailAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if(!(editTextLoginEmailAddress.getText().toString().isEmpty())){
                    emailFilled = true;
                }else{
                    emailFilled = false;
                    buttonLogin.setEnabled(false);
                }

                if(emailFilled && passwordFilled){
                    buttonLogin.setEnabled(true);
                }

            }
        });

        editTextLoginPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if(!(editTextLoginPassword.getText().toString().isEmpty()) && verifyPassword(editTextLoginPassword.getText().toString())){
                    passwordFilled = true;
                }else{
                    passwordFilled = false;
                    buttonLogin.setEnabled(false);
                }

                if(emailFilled && passwordFilled){
                    buttonLogin.setEnabled(true);
                }

                System.out.println("Password listener " + emailFilled + "  " + passwordFilled);

            }
        });

    }

    /*
    Verify that the password is pasta a set length
     */
    private boolean verifyPassword(String password){
        if(password.length() < PASS_LENGTH){
            return false;
        }else{
            return true;
        }
    }


    /*
    Listener for the login button
     */
    private void clickLoginListener(){
        //Click the login button
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Progress Bar is turned on
                progressBarLogin.setVisibility(View.VISIBLE);
                email = editTextLoginEmailAddress.getText().toString().trim();
                password = editTextLoginPassword.getText().toString().trim();
                login(email,password);
            }
        });
    }

    /*
    Creates all UI elements
     */
    private void createElements(){
        editTextLoginEmailAddress = findViewById(R.id.editTextLoginEmailAddress);
        editTextLoginPassword = findViewById(R.id.editTextLoginPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonLogin.setEnabled(false);
        progressBarLogin = findViewById(R.id.progressBarLogin);
        fAuth = FirebaseAuth.getInstance();
    }



    /*
    When the Sign Up text is clicked on the login page this method is called
     */
    public void registerAccountPage(View view){
        startActivity(new Intent(getApplicationContext(), Register.class));
        finish();
    }
}