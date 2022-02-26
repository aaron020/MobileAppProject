package com.example.mobileapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {
    EditText editTextLoginEmailAddress;
    EditText editTextPassword;
    Button buttonLogin;
    ProgressBar progressBarLogin;
    private String email;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createElements();







        //Click the login button
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Progress Bar is turned on
                progressBarLogin.setVisibility(View.VISIBLE);



                email = editTextLoginEmailAddress.getText().toString().trim();
                password = editTextPassword.getText().toString().trim();

                System.out.println(email + "   " + password);

            }
        });





    }





    private void createElements(){
        editTextLoginEmailAddress = findViewById(R.id.editTextLoginEmailAddress);
        editTextPassword = findViewById(R.id.editTextLoginPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        progressBarLogin = findViewById(R.id.progressBarLogin);
    }



    //When the Sign Up text is clicked on the login page this method is called
    public void registerAccountPage(View view){
        startActivity(new Intent(getApplicationContext(), Register.class));
        finish();
    }
}