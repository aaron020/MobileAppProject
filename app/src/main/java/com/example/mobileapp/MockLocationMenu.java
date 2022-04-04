package com.example.mobileapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MockLocationMenu extends AppCompatActivity {
    Button buttonMockNew, buttonMockSaved;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mock_location_menu);
        createElements();
        newMockClicked();
    }

    private void createElements(){
        buttonMockNew = findViewById(R.id.buttonMockNew);
        buttonMockSaved = findViewById(R.id.buttonMockSaved);
    }

    private void newMockClicked(){
        buttonMockNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MockLocationMenu.this, MockLocationMap.class);
                startActivity(intent);
                finish();
            }
        });
    }
}