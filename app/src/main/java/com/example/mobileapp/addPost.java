package com.example.mobileapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class addPost extends AppCompatActivity {
    private EditText editTextPostTitle , editTextPostDescription;
    private Button buttonPost;
    private ImageButton buttonAddPostToMLP;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        createElements();
        addPost();
        returnToMyLocationPost();
    }

    private void createElements(){
        editTextPostTitle = findViewById(R.id.editTextPostTitle);
        editTextPostDescription = findViewById(R.id.editTextPostDescription);
        buttonPost = findViewById(R.id.buttonPost);
        fStore = FirebaseFirestore.getInstance();
        buttonAddPostToMLP = findViewById(R.id.buttonAddPostToMLP);

    }

    private void returnToMyLocationPost(){
        buttonAddPostToMLP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(addPost.this, MyLocationPost.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void addPost(){

        buttonPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = editTextPostTitle.getText().toString();
                String description = editTextPostDescription.getText().toString();
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();


                if(TextUtils.isEmpty(title)){
                    Toast.makeText(addPost.this,"Enter a title", Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(description)){
                    Toast.makeText(addPost.this,"Enter a description", Toast.LENGTH_SHORT).show();
                }else{
                    DocumentReference dr = fStore.getInstance().collection("posts").document();
                    String id = dr.getId();
                    System.out.println(id);
                    Post newPost = new Post(id,title,description,userId,Location.latitude,Location.longitude);
                    fStore.collection("posts").document(id).set(newPost).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(addPost.this,"Posted!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}