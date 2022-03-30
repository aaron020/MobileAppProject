package com.example.mobileapp;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Post implements Comparable<Post>{
    private String id;
    private String title;
    private String description;
    private String userId;
    private String username;
    private double longitude;
    private double latitude;
    private double timestamp;


    public Post(){
    }

    public Post(String id,String username,String title, String description, String userId, double latitude, double longitude) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.userId = userId;
        this.longitude = longitude;
        this.latitude = latitude;
        this.username = username;
        this.timestamp = System.currentTimeMillis();
//        UserData user = new UserData();
//        user.findUsername(userId);
//        userName = user.getUsername();
    }


    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getUserId() {
        return userId;
    }


    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public String getUsername() {
        return username;
    }

    public double getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", userId='" + userId + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }

    @Override
    public int compareTo(Post post) {
        if(this.timestamp > post.timestamp){
            return -1;
        }else if (this.timestamp < post.timestamp){
            return 1;
        }
        return 0;
    }
}
