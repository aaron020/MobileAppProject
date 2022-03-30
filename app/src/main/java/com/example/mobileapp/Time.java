package com.example.mobileapp;
//Time using epoch time
public class Time {
    private double currentTime;
    private final double NOW = 60000;
    private final double MINS = 60000;
    private final double HOUR = 3600000;
    private final double DAY = 86400000;


    public Time(){
        currentTime = System.currentTimeMillis();
    }

    //Gets an approx time from when a post was made as string
    public String getApproxTime(double timePosted){
        double difference = currentTime - timePosted;
        //hmm
        if(difference < 0){
            return "Time Traveler";
        }
        if(difference < NOW){
            return "Now";
        }
        if(difference < MINS * 10){
            return "10 mins ago";
        }
        if(difference < MINS * 30){
            return "30 mins ago";
        }
        if(difference < HOUR){
            return "1 hour ago";
        }
        if(difference < HOUR*2){
            return  "2 hours ago";
        }
        if(difference < HOUR * 5){
            return "5 hours ago";
        }
        if(difference < HOUR*12){
            return "12 hours ago";
        }
        if(difference < DAY){
            return "1 day ago";
        }
        if(difference < DAY*2){
            return "2 days ago";
        }
        if(difference < DAY*5){
            return "5 days ago";
        }
        if(difference < DAY*7){
            return "1 week ago";
        }
        if(difference < DAY*28){
            return "1 month ago";
        }
        if(difference < DAY*84){
            return "3 months ago";
        }
        if(difference < DAY*168){
            return "6 months ago";
        }
        if(difference < DAY*365){
            return "1 year ago";
        }
        if(difference < DAY*730){
            return "2 years ago";
        }
        return "A long time ago";



    }
}
