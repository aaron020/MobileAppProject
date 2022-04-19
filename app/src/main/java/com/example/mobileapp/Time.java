package com.example.mobileapp;
//Time using epoch time
public class Time {
    private double currentTime;
    private final double FIVE = 300000;
    private final double HOUR = 3600000;
    private final double DAY = 86400000;


    public Time(){
        currentTime = System.currentTimeMillis();
    }

    //Gets an approx time from when a post was made as string
    public String getApproxTime(long timePosted) {
        //Returns the time difference
        return android.text.format.DateUtils.getRelativeTimeSpanString(timePosted).toString();
    }

}
