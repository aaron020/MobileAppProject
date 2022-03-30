package com.example.mobileapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;

public class UserData extends AppCompatActivity {
    private String userName;
    private DocumentReference df;
    private FirebaseFirestore fStore;

    public UserData(){
        fStore = FirebaseFirestore.getInstance();

    }

    public String getUsername() {
        return userName;
    }

    public void findUsername(String userId){
        DocumentReference df;
        FirebaseFirestore fStore = FirebaseFirestore.getInstance();
        df = fStore.collection("users").document(userId);
        df.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                userName = value.getString("username");
            }
        });
    }
}
