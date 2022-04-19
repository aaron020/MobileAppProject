package com.example.mobileapp;
/*
Class to store saved locations
 */
public class savedLocation {
    private double longitude;
    private double latitude;
    private String title;
    private String userId;

    public savedLocation(double longitude, double latitude, String title, String userId) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.title = title;
        this.userId = userId;
    }

    public savedLocation(){}

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public String getTitle() {
        return title;
    }

    public String getUserId() {
        return userId;
    }
}
