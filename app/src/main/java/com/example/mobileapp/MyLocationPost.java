package com.example.mobileapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;


import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;
import java.util.List;

public class MyLocationPost extends AppCompatActivity implements MyAdapter.OnPostListener {
    private ArrayList<String> titles = new ArrayList<>();
    private ArrayList<String> descriptions = new ArrayList<>();
    private ArrayList<String> userData = new ArrayList<>();
    private RecyclerView recyclerView;
    private Button addPost;
    private ImageButton buttonMLPToMenu;
    private FirebaseFirestore fStore;
    private ArrayList<Post> posts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_location_post);
        createElements();
        addPostClick();
        EventChange();
        MLPToMenu();






    }

    private void EventChange() {
        fStore.collection("posts").orderBy("title",Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null){
                    Log.d(TAG, "error : " + error.getMessage());
                }

                for(DocumentChange dc : value.getDocumentChanges()){
                    if(dc.getType() == DocumentChange.Type.ADDED){
                        posts.add(dc.getDocument().toObject(Post.class));
                    }
                }
                recyclerView = findViewById(R.id.recyclerviewMyLocation);
                MyAdapter myAdapter = new MyAdapter(posts,MyLocationPost.this, MyLocationPost.this);
                recyclerView.setAdapter(myAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(MyLocationPost.this));
            }
        });

    }


    private void createElements(){
        addPost = findViewById(R.id.buttonAddPost);
        fStore = FirebaseFirestore.getInstance();
        posts = new ArrayList<>();
        buttonMLPToMenu = findViewById(R.id.buttonMLPToMenu);
    }

    private void MLPToMenu(){
        buttonMLPToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyLocationPost.this, Home.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void addPostClick(){
        addPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyLocationPost.this, addPost.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onPostClick(int position) {
        System.out.println(position);
        Intent i = new Intent(this, OpenPost.class);
        startActivity(i);

    }
}