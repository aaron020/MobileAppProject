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
import android.widget.CheckBox;
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
/*
This class deals with adding a new post to the DB
 */
public class addPost extends AppCompatActivity {
    private EditText editTextPostTitle , editTextPostDescription;
    private TextView textViewPostTitle, textViewPostDescription, textViewTextAmount;
    private Spinner spinnerTypeOfPost;
    private CheckBox checkBoxExactLocation;
    private Button buttonPost;
    private ImageButton buttonAddPostToMLP;
    private String username;
    //The type of post - this changes which icon is used
    private final String[] typeOfPosts = {"Choose","Helpful","Informative","Curious","Event","Academic","Weather","Other"};
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
    /*
    Spinner element listener to check which type of post the user would like to use
     */
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
    /*
    Changes the visibility of elements on the page depending on if a type of post is choosen or not
     */
    private void visiblity(int i){
        editTextPostTitle.setVisibility(i);
        textViewPostTitle.setVisibility(i);
        editTextPostDescription.setVisibility(i);
        textViewPostDescription.setVisibility(i);
        buttonPost.setVisibility(i);
        textViewTextAmount.setVisibility(i);
        checkBoxExactLocation.setVisibility(i);
    }
    /*
    Text listner for the Title editText - this gives the user an idea of how many characters they can
    enter
     */
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



    /*
    Create all elements when the page starts
     */
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
        checkBoxExactLocation = findViewById(R.id.checkBoxExactLocation);

    }
    /*
    Brings the user back to the Posts screen, UserPosts bundle is set to false to indicate that
    the Post screen should display post from all users rather than just the user logged in
     */
    private void returnToMyLocationPost(){
        buttonAddPostToMLP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(addPost.this, MyLocationPost.class);
                Bundle b = new Bundle();
                b.putBoolean("UserPosts",false);
                intent.putExtras(b);
                startActivity(intent);
                finish();
            }
        });
    }
    /*
    Listener to check if the Post button is clicked - also checked that the user has not set
    any empty values
     */
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
    /*
    Distort the users location
     */
    private double distortLocation(double coord){
        double distortion = Math.random()/Settings.Distort_Factor;
        return coord + distortion;
    }

    /*
    Adds the new post to the firebase database
     */
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
                Post newPost;
                if(checkBoxExactLocation.isChecked()){
                    newPost = new Post(id,username,title,description,userId,Location.latitude,Location.longitude, typeChosen);
                }else{
                    //Distort the users location
                    newPost = new Post(id,username,title,description,userId,distortLocation(Location.latitude),distortLocation(Location.longitude), typeChosen);
                }
                //Add the new post to the posts collection on firebase
                fStore.collection("posts").document(id).set(newPost).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        editTextPostTitle.getText().clear();
                        editTextPostDescription.getText().clear();
                        Toast.makeText(addPost.this,"Posted!", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
    }
}