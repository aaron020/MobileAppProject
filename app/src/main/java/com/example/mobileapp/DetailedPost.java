package com.example.mobileapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;

public class DetailedPost extends AppCompatActivity {
    private Bundle bundle = null;
    private TextView textViewTitleDP, textViewDescriptionDP, textViewUserDataDP;
    private ArrayList<Comment> comments;
    private RecyclerView recyclerView;
    private ImageButton imageButtonMore, imageButtonLess, buttonDPtoMLP;
    private EditText editTextComment;
    private Post post;
    private FirebaseFirestore fStore;
    private Button buttonAddComment;
    private ListenerRegistration listenerRegistration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_post);
        createElements();
        addComment();
        bundle = getIntent().getExtras();
        clickLess();
        clickMore();
        clickBack();
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
                String commentText = editTextComment.getText().toString();
                if(TextUtils.isEmpty(commentText)){
                    Toast.makeText(DetailedPost.this,"Please Add Some Text!", Toast.LENGTH_SHORT).show();
                }else{
                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    addToDb(userId, post.getId(), commentText);
                    editTextComment.getText().clear();
                }

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
                        Toast.makeText(DetailedPost.this,"Posted!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }


    private void EventChange() {
        listenerRegistration = fStore.collection("comments").whereEqualTo("postId",post.getId())
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

    @Override
    protected void onStop() {
        super.onStop();
        if(listenerRegistration != null){
            listenerRegistration.remove();
        }
    }

    private void clickLess(){
        imageButtonLess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textViewDescriptionDP.setText(post.getShortDesc());
                imageButtonLess.setVisibility(View.GONE);
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
                imageButtonMore.setVisibility(View.GONE);
            }
        });
    }

    private void clickBack(){
        buttonDPtoMLP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailedPost.this, MyLocationPost.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void setAdapter() {
        recyclerView = findViewById(R.id.recyclerviewDP);
        Collections.sort(comments);
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
        buttonDPtoMLP = findViewById(R.id.buttonDPtoMLP);
        editTextComment = findViewById(R.id.editTextComment);
    }
}