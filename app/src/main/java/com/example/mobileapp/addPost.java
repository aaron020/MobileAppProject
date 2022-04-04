package com.example.mobileapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;

public class addPost extends AppCompatActivity {
    private EditText editTextPostTitle , editTextPostDescription;
    private TextView textViewPostTitle, textViewPostDescription, textViewTextAmount;
    private Spinner spinnerTypeOfPost;
    private Button buttonPost;
    private ImageButton buttonAddPostToMLP;
    private String username;
    private String[] typeOfPosts = {"Choose","Helpful","Informative","Curious","Event","Academic","Weather","Other"};
    private int typeChosen;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        createElements();
        addPost();
        returnToMyLocationPost();
        itemSelected();
        textListeners();
    }

    private void itemSelected(){
        spinnerTypeOfPost.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(adapterView.getItemAtPosition(i) != typeOfPosts[0]){
                    visiblity(View.VISIBLE);
                    typeChosen = i;
                }else{
                    visiblity(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void visiblity(int i){
        editTextPostTitle.setVisibility(i);
        textViewPostTitle.setVisibility(i);
        editTextPostDescription.setVisibility(i);
        textViewPostDescription.setVisibility(i);
        buttonPost.setVisibility(i);
        textViewTextAmount.setVisibility(i);
    }

    private void textListeners(){
        editTextPostTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                textViewTextAmount.setText(editable.toString().length() + "/25");
            }
        });

    }



    private void createElements(){
        editTextPostTitle = findViewById(R.id.editTextPostTitle);
        textViewPostTitle = findViewById(R.id.textViewPostTitle);
        textViewPostDescription = findViewById(R.id.textViewPostTitle);
        editTextPostDescription = findViewById(R.id.editTextPostDescription);
        buttonPost = findViewById(R.id.buttonPost);
        fStore = FirebaseFirestore.getInstance();
        buttonAddPostToMLP = findViewById(R.id.buttonAddPostToMLP);
        spinnerTypeOfPost = findViewById(R.id.spinnerTypeOfPost);
        textViewTextAmount = findViewById(R.id.textViewTextAmount);
        ArrayAdapter types = new ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,typeOfPosts);
        spinnerTypeOfPost.setAdapter(types);

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
                    addToDb(userId,title,description);
                }
            }
        });
    }


    public void addToDb(String userId,String title, String description){
        DocumentReference docRead;
        docRead = fStore.collection("users").document(userId);
        docRead.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                //Get username then add all data to db
                username = value.getString("username");
                DocumentReference dr = fStore.getInstance().collection("posts").document();
                String id = dr.getId();
                System.out.println(id);
                Post newPost = new Post(id,username,title,description,userId,Location.latitude,Location.longitude, typeChosen);
                fStore.collection("posts").document(id).set(newPost).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(addPost.this,"Posted!", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
    }
}