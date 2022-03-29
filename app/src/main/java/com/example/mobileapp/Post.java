package com.example.mobileapp;

import java.util.HashMap;
import java.util.Map;

public class Post {
    private String id;
    private String title;
    private String description;
    private String userId;
    private double longitude;
    private double latitude;


    public Post(){
    }

    public Post(String id,String title, String description, String userId, double latitude, double longitude) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.userId = userId;
        this.longitude = longitude;
        this.latitude = latitude;
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
}
