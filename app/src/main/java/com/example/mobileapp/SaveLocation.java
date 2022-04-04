package com.example.mobileapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;

public class SaveLocation extends AppCompatActivity implements SavedAdapter.OnPostListener{
    private RecyclerView recyclerView;
    private FirebaseFirestore fStore;
    private ArrayList<savedLocation> savedLocations;
    private String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_location);
        createElements();
        EventChange();
    }

    private void createElements(){
        fStore = FirebaseFirestore.getInstance();
        savedLocations = new ArrayList<>();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }


    private void EventChange() {
        fStore.collection("locations").whereEqualTo("userId",userId)
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
                        recyclerView = findViewById(R.id.recyclerviewSavedLocation);
                        SavedAdapter SavedAdapter = new SavedAdapter(savedLocations,SaveLocation.this, SaveLocation.this);
                        recyclerView.setAdapter(SavedAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(SaveLocation.this));
                    }
                });

    }

    @Override
    public void onPostClick(int position) {
        System.out.println(position);
    }
}