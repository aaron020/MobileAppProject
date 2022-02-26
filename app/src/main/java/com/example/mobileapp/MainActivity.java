package com.example.mobileapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println("Hello World");
    }

    //When the Sign Up text is clicked on the login page this method is called
    public void registerAccountPage(View view){
        startActivity(new Intent(getApplicationContext(), Register.class));
        finish();
    }
}