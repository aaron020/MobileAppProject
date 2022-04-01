package com.example.mobileapp;
//Formula adapted from https://dzone.com/articles/distance-calculation-using-3

public class Distance {

    public double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        System.out.println(dist * 1.609344);
        return dist * 1.609344;
    }
    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }
    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    public String distanceToString(double lat1, double lon1, double lat2, double lon2){
        double distance = distance(lat1,lon1,lat2,lon2);
        for(int i = 1; i < 150; i++){
            if(distance < i){
                return "~" + i + "km away";
            }
        }
        return "far away";
    }

}
