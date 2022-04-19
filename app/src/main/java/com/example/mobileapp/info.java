package com.example.mobileapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
/*
Info section which uses two fragments to store information
 */
public class info extends AppCompatActivity {
    Button buttonPrevious, buttonNext, buttonInfoMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        createElements();
        changeInfo(new info_pg1());
        clickNext();
        clickPrevious();
        clickMenu();
    }

    private void createElements(){
        buttonPrevious = findViewById(R.id.buttonPrevious);
        buttonNext = findViewById(R.id.buttonNext);
        buttonInfoMenu = findViewById(R.id.buttonInfoMenu);
    }

    private void clickNext(){
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeInfo(new info_pg2());
            }
        });
    }

    private void changeInfo(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayoutInfo, fragment);
        fragmentTransaction.commit();
    }

    private void clickMenu(){
        buttonInfoMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(info.this, Home.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void clickPrevious(){
        buttonPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeInfo(new info_pg1());
            }
        });
    }
}