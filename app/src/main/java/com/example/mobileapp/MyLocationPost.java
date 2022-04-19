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
import com.google.firebase.auth.FirebaseAuth;
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
/*
Main page of the app, allows the user to see posts from users around them, also allows the user to
see just their own posts
 */
public class MyLocationPost extends AppCompatActivity implements MyAdapter.OnPostListener {
    private RecyclerView recyclerView;

    private FloatingActionsMenu FAButtonMyLocation;
    private FloatingActionButton FAButtonMenu,FAButtonLocation,FAButtonAdd, FAButtonMockLocation;



    private FirebaseFirestore fStore;
    private ArrayList<Post> posts;
    private Distance dist;

    private AlertDialog.Builder dialogBox;
    private AlertDialog dialog;
    private Button buttonNewMockLocation, buttonSavedMockLocation, buttonExitDialog;
    private ListenerRegistration listenerRegistration;

    private TextView textViewNoPosts;
    private Boolean userPosts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_location_post);
        createElements();
        //If UserPosts bundle is true then only the users posts will displayed
        Bundle bundle = getIntent().getExtras();
        userPosts = bundle.getBoolean("UserPosts");

        if(userPosts){
            FloatingButtons(View.GONE);
            //Gets all and only the user thats logged in posts from the db
            UserPosts();
        }else{
            FloatingButtons(View.VISIBLE);
            EventChange();
        }


        floatingButton();
    }

    private void FloatingButtons(int i){
        FAButtonAdd.setVisibility(i);
        FAButtonLocation.setVisibility(i);
        FAButtonMockLocation.setVisibility(i);
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
    /*
    If user clicks on new mock location a dialog box pops up asking them if they would like to
    view their saved locations or create a new mock location
     */
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



/*
This function gets all posts from the db and stores them in an arraylist
 */
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
                //Removes any post that is outside the maxiumum distance set in settings
                posts = manageDistance(postsFromDB);
                //Sorts the posts by time
                Collections.sort(posts);
                if(posts.size() == 0){
                    //There is no posts - tell the user this
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


    private void UserPosts() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        listenerRegistration = fStore.collection("posts").whereEqualTo("userId",userId)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error != null){
                            Log.d(TAG, "error : " + error.getMessage());
                        }
                        ArrayList<Post> postsFromDB = new ArrayList<>();
                        for(DocumentChange dc : value.getDocumentChanges()){
                            if(dc.getType() == DocumentChange.Type.ADDED){
                                posts.add(dc.getDocument().toObject(Post.class));
                            }
                        }
                        recyclerView = findViewById(R.id.recyclerviewMyLocation);
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
    /*
    Loops through the array of posts and calculates how far away each post is
    only adds posts that are in the set distance and then returns the new arraylist
     */
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
    }



    @Override
    public void onPostClick(int position) {
        System.out.println(position);
        Intent i = new Intent(this, DetailedPost.class);
        Bundle b = new Bundle();
        b.putBoolean("UserDP",userPosts);
        i.putExtras(b);
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