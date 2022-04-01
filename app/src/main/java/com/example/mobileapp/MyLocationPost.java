package com.example.mobileapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;


import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MyLocationPost extends AppCompatActivity implements MyAdapter.OnPostListener {
    private RecyclerView recyclerView;

    private FloatingActionsMenu FAButtonMyLocation;
    private FloatingActionButton FAButtonMenu,FAButtonLocation,FAButtonAdd;
    private Animation open, close, fromBottom, toBottom;
    private boolean isOpen;

    private FirebaseFirestore fStore;
    private ArrayList<Post> posts;
    private Distance dist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_location_post);
        createElements();
        EventChange();
        floatingButton();

    }

    private void floatingButton(){
        FAButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyLocationPost.this, addPost.class);
                startActivity(intent);
                finish();
            }
        });
        FAButtonMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyLocationPost.this, Home.class);
                startActivity(intent);
                finish();
            }
        });
        FAButtonLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyLocationPost.this, myLocationMap.class);
                intent.putExtra("posts", posts);
                startActivity(intent);
                finish();
            }
        });
    }

    private void EventChange() {
        fStore.collection("posts")
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
                //Sort posts depending on time of posting
                Collections.sort(posts);
                manageDistance();
                MyAdapter myAdapter = new MyAdapter(posts,MyLocationPost.this, MyLocationPost.this);
                recyclerView.setAdapter(myAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(MyLocationPost.this));
            }
        });

    }

    private void manageDistance(){
        for(int i = 0; i < posts.size(); i++){
            if(dist.distance(Location.latitude, Location.longitude, posts.get(i).getLatitude(),posts.get(i).getLongitude()) > Settings.Distance){
                posts.remove(i);
            }
        }
    }




    private void createElements(){
        fStore = FirebaseFirestore.getInstance();
        posts = new ArrayList<>();
        dist = new Distance();
        FAButtonMyLocation = findViewById(R.id.FAButtonMyLocation);
        FAButtonMenu = findViewById(R.id.FAButtonMenu);
        FAButtonLocation  = findViewById(R.id.FAButtonLocation);
        FAButtonAdd = findViewById(R.id.FAButtonAdd);
        isOpen = false;

    }



    @Override
    public void onPostClick(int position) {
        System.out.println(position);
        Intent i = new Intent(this, DetailedPost.class);
        //Send the info of the post to the next activity
        i.putExtra("post", posts.get(position));
        startActivity(i);

    }
}