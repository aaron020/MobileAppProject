package com.example.mobileapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.Set;

public class SettingsActivity extends AppCompatActivity {
    Button buttonSaveChanges;
    RadioGroup radio_distance, radio_distortion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        buttonSaveChanges = findViewById(R.id.buttonSaveChanges);
        radio_distance = findViewById(R.id.radio_distance);
        radio_distortion = findViewById(R.id.radio_distortion);
        buttonSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //nothing is checked
                if(radio_distance.getCheckedRadioButtonId() == -1){
                    Toast.makeText(SettingsActivity.this,"Select A Distance Please:) ", Toast.LENGTH_SHORT).show();
                }else if(radio_distortion.getCheckedRadioButtonId() == -1){
                    Toast.makeText(SettingsActivity.this,"Select A Distortion Factor Please:) ", Toast.LENGTH_SHORT).show();
                }else{
                    switch (radio_distance.getCheckedRadioButtonId()){
                        case R.id.radio_10km:
                            Settings.Distance = 10;
                            break;
                        case R.id.radio_20km:
                            Settings.Distance= 20;
                            break;
                        case R.id.radio_50km:
                            Settings.Distance = 50;
                            break;
                        case R.id.radio_100km:
                            Settings.Distance = 100;
                            break;
                    }

                    switch (radio_distortion.getCheckedRadioButtonId()){
                        case R.id.radio_low:
                            Settings.Distort_Factor = 5000;
                            break;
                        case R.id.radio_medium:
                            Settings.Distort_Factor = 1000;
                            break;
                        case R.id.radio_high:
                            Settings.Distort_Factor = 700;
                            break;
                    }
                    Intent intent = new Intent(SettingsActivity.this, Home.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }



}