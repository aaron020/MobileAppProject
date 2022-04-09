package com.example.mobileapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class SaveLocation extends AppCompatActivity implements SavedAdapter.OnPostListener{
    private RecyclerView recyclerView;
    private FirebaseFirestore fStore;
    private ArrayList<savedLocation> savedLocations;
    private String userId;

    private FloatingActionsMenu FAButtonSV;
    private FloatingActionButton FAButtonMenuSV,FAButtonMockLocationSV;
    private TextView textViewNoSaved;
    private ListenerRegistration listenerRegistration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_location);
        createElements();
        EventChange();
        clickFLoatingButtons();
    }

    private void createElements(){
        fStore = FirebaseFirestore.getInstance();
        savedLocations = new ArrayList<>();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FAButtonSV = findViewById(R.id.FAButtonSV);
        FAButtonMenuSV = findViewById(R.id.FAButtonMenuSV);
        FAButtonMockLocationSV = findViewById(R.id.FAButtonMockLocationSV);
        textViewNoSaved = findViewById(R.id.textViewNoSaved);
    }

    private void clickFLoatingButtons(){
        FAButtonMenuSV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SaveLocation.this, Home.class);
                startActivity(intent);
                finish();
            }
        });

        FAButtonMockLocationSV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SaveLocation.this, MockLocationMap.class);
                startActivity(intent);
                finish();
            }
        });
    }


    private void EventChange() {
        listenerRegistration = fStore.collection("locations").whereEqualTo("userId",userId)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error != null){
                            Log.d(TAG, "error : " + error.getMessage());
                        }

                        for(DocumentChange dc : value.getDocumentChanges()){
                            if(dc.getType() == DocumentChange.Type.ADDED){
                                savedLocations.add(dc.getDocument().toObject(savedLocation.class));
                            }
                        }
                        if(savedLocations.size() == 0){
                            textViewNoSaved.setVisibility(View.VISIBLE);
                        }else{
                            textViewNoSaved.setVisibility(View.GONE);
                        }
                        Collections.sort(savedLocations, new Comparator<savedLocation>() {
                            public int compare(savedLocation v1, savedLocation v2) {
                                return v1.getTitle().compareTo(v2.getTitle());
                            }
                        });

                        recyclerView = findViewById(R.id.recyclerviewSavedLocation);
                        SavedAdapter SavedAdapter = new SavedAdapter(savedLocations,SaveLocation.this, SaveLocation.this);
                        recyclerView.setAdapter(SavedAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(SaveLocation.this));
                    }
                });

    }



    @Override
    public void onPostClick(int position) {
        Location.latitude = savedLocations.get(position).getLatitude();
        Location.longitude = savedLocations.get(position).getLongitude();
        Toast.makeText(SaveLocation.this, "Opening Location @" + Location.latitude + ", " + Location.longitude, Toast.LENGTH_SHORT).show();
        //Switch to the post page
        Intent intent = new Intent(SaveLocation.this, MyLocationPost.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(listenerRegistration != null){
            listenerRegistration.remove();
        }
    }
}