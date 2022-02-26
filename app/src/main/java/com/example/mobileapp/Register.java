package com.example.mobileapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    //When the Sign In text is clicked on the login page this method is called
    public void signInAccountPage(View view){
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }
}