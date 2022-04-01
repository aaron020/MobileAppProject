package com.example.mobileapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;

public class DetailedPost extends AppCompatActivity {
    private Bundle bundle = null;
    private TextView textViewTitleDP, textViewDescriptionDP, textViewUserDataDP;
    private ArrayList<Comment> comments;
    private RecyclerView recyclerView;
    private ImageButton imageButtonMore, imageButtonLess;
    private Post post;
    private FirebaseFirestore fStore;
    private Button buttonAddComment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_post);
        createElements();
        addComment();
        bundle = getIntent().getExtras();
        clickLess();
        clickMore();
        if(bundle != null){
            post = bundle.getParcelable("post");
            textViewTitleDP.setText(post.getTitle());
            textViewDescriptionDP.setText(post.getDescription());
            if(post.getDescription().length() > 150){
                imageButtonLess.setVisibility(View.VISIBLE);
            }
            Time t = new Time();
            textViewUserDataDP.setText(post.getUsername() + "   -   " + t.getApproxTime(post.getTimestamp()));
        }

        comments = new ArrayList<>();
        EventChange();
    }


    private void addComment(){
        buttonAddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DetailedPost.this, addComment.class);
                //Send the info of the post to the next activity
                i.putExtra("postId", post.getId());
                startActivity(i);
            }
        });
    }


    private void EventChange() {
        fStore.collection("comments").whereEqualTo("postId",post.getId())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error != null){
                            Log.d(TAG, "error : " + error.getMessage());
                        }

                        for(DocumentChange dc : value.getDocumentChanges()){
                            if(dc.getType() == DocumentChange.Type.ADDED){
                                comments.add(dc.getDocument().toObject(Comment.class));
                                System.out.println("Found");
                            }
                        }

                        setAdapter();
                    }
                });

    }

    private void clickLess(){
        imageButtonLess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textViewDescriptionDP.setText(post.getShortDesc());
                imageButtonLess.setVisibility(View.INVISIBLE);
                imageButtonMore.setVisibility(View.VISIBLE);
            }
        });
    }

    private void clickMore(){
        imageButtonMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textViewDescriptionDP.setText(post.getDescription());
                imageButtonLess.setVisibility(View.VISIBLE);
                imageButtonMore.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void setAdapter() {
        recyclerView = findViewById(R.id.recyclerviewDP);
        AdapterComments adapterComments = new AdapterComments(comments,this);
        recyclerView.setAdapter(adapterComments);
        recyclerView.setLayoutManager(new LinearLayoutManager(DetailedPost.this));

    }

    private void createElements(){
        textViewTitleDP = findViewById(R.id.textViewTitleDP);
        textViewDescriptionDP = findViewById(R.id.textViewDescriptionDP);
        textViewUserDataDP = findViewById(R.id.textViewUserDataDP);
        imageButtonMore = findViewById(R.id.imageButtonMore);
        imageButtonLess = findViewById(R.id.imageButtonLess);
        fStore = FirebaseFirestore.getInstance();
        buttonAddComment = findViewById(R.id.buttonAddComment);
    }
}