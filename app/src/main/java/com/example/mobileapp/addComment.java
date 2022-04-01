package com.example.mobileapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class addComment extends AppCompatActivity {
    Bundle bundle = null;
    private String postId;
    private EditText editTextTextCommentText;
    private Button buttonCommentDB;
    private FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_comment);
        bundle = getIntent().getExtras();

        if(bundle != null){
            postId = bundle.getString("postId");
        }
        createElements();
        addComment();

    }

    private void addComment(){
        buttonCommentDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = editTextTextCommentText.getText().toString();
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                addToDb(userId,postId,text);

            }
        });
    }


    public void addToDb(String userId,String postId, String text){
        DocumentReference docRead;
        docRead = fStore.collection("users").document(userId);
        docRead.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                //Get username then add all data to db
                String username = value.getString("username");
                DocumentReference dr = fStore.getInstance().collection("comments").document();
                String id = dr.getId();
                System.out.println(id);
                Comment comment = new Comment(username,postId,text);
                fStore.collection("comments").document(id).set(comment).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(addComment.this,"Posted!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }


    private void createElements(){
        editTextTextCommentText = findViewById(R.id.editTextTextCommentText);
        buttonCommentDB = findViewById(R.id.buttonCommentDB);
        fStore = FirebaseFirestore.getInstance();
    }




}