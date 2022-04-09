package com.example.mobileapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.VolumeShaper;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MyLocationPost extends AppCompatActivity implements MyAdapter.OnPostListener {
    private RecyclerView recyclerView;

    private FloatingActionsMenu FAButtonMyLocation;
    private FloatingActionButton FAButtonMenu,FAButtonLocation,FAButtonAdd, FAButtonMockLocation;
    //private Animation open, close, fromBottom, toBottom;
    private boolean isOpen;

    private FirebaseFirestore fStore;
    private ArrayList<Post> posts;
    private Distance dist;

    private AlertDialog.Builder dialogBox;
    private AlertDialog dialog;
    private Button buttonNewMockLocation, buttonSavedMockLocation, buttonExitDialog;
    private ListenerRegistration listenerRegistration;

    private TextView textViewNoPosts;

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

        FAButtonMockLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newDialog();
            }
        });
    }

    private void newDialog(){
        dialogBox = new AlertDialog.Builder(this);
        final View MockLocationDialog = getLayoutInflater().inflate(R.layout.mock_location_dialog, null);
        dialogBox.setView(MockLocationDialog);
        dialog = dialogBox.create();
        dialog.show();
        buttonNewMockLocation = MockLocationDialog.findViewById(R.id.buttonNewMockLocation);
        buttonSavedMockLocation = MockLocationDialog.findViewById(R.id.buttonSavedMockLocation);
        buttonExitDialog = MockLocationDialog.findViewById(R.id.buttonExitDialog);

        buttonNewMockLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyLocationPost.this, MockLocationMap.class);
                startActivity(intent);
                finish();
            }
        });

        buttonSavedMockLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyLocationPost.this, SaveLocation.class);
                startActivity(intent);
                finish();
            }
        });

        buttonExitDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }




    private void EventChange() {
        listenerRegistration = fStore.collection("posts")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null){
                    Log.d(TAG, "error : " + error.getMessage());
                }
                ArrayList<Post> postsFromDB = new ArrayList<>();
                for(DocumentChange dc : value.getDocumentChanges()){
                    if(dc.getType() == DocumentChange.Type.ADDED){
                        postsFromDB.add(dc.getDocument().toObject(Post.class));
                    }
                }

                recyclerView = findViewById(R.id.recyclerviewMyLocation);
                posts = manageDistance(postsFromDB);
                Collections.sort(posts);
                if(posts.size() == 0){
                    textViewNoPosts.setVisibility(View.VISIBLE);
                }else{
                    textViewNoPosts.setVisibility(View.GONE);
                }
                MyAdapter myAdapter = new MyAdapter(posts,MyLocationPost.this, MyLocationPost.this);
                recyclerView.setAdapter(myAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(MyLocationPost.this));
            }
        });

    }

    private ArrayList<Post> manageDistance(ArrayList<Post> posts_In){
        Distance dist = new Distance();
        ArrayList<Post> postsArranged = new ArrayList<>();
        for(int i = 0; i < posts_In.size(); i++){
            if(dist.distance(Location.latitude, Location.longitude, posts_In.get(i).getLatitude(),posts_In.get(i).getLongitude()) <= Settings.Distance){
                postsArranged.add(posts_In.get(i));
            }
        }
        return postsArranged;
    }






    private void createElements(){
        fStore = FirebaseFirestore.getInstance();
        posts = new ArrayList<>();
        dist = new Distance();
        FAButtonMyLocation = findViewById(R.id.FAButtonMyLocation);
        FAButtonMenu = findViewById(R.id.FAButtonMenu);
        FAButtonLocation  = findViewById(R.id.FAButtonLocation);
        FAButtonMockLocation = findViewById(R.id.FAButtonMockLocation);
        FAButtonAdd = findViewById(R.id.FAButtonAdd);
        textViewNoPosts = findViewById(R.id.textViewNoPosts);
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

    @Override
    protected void onStop() {
        super.onStop();
        if(listenerRegistration != null){
            listenerRegistration.remove();
        }
    }



}