package com.example.mobileapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Home extends AppCompatActivity {
    private Button buttonHomeLogout;
    private Button buttonHomeMyLocation, buttonHomeNewLocation, buttonHomeInfo, buttonHomeSettings, buttonHomeMyProfile;
    private ProgressBar progressBar;
    private LocationRequest locationRequest;
    private Boolean userPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        createElements();
        clickLogout();
        clickMyLocation();
        clickNewLocation();
        clickInfo();
        clickSettings();
        clickMyProfile();
    }


    private void clickLogout() {
        buttonHomeLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(Home.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }



    private void clickSettings() {
        buttonHomeSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, SettingsActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void clickMyLocation() {
        buttonHomeMyLocation.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                userPost = false;
                //getCurrentLocation();
                getCurrentLocation();
            }
        });

    }

    private void clickNewLocation(){
        buttonHomeNewLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, SaveLocation.class);
//                Bundle b = ActivityOptions.makeSceneTransitionAnimation(Home.this).toBundle();
                startActivity(intent);
                finish();
            }
        });
    }

    private void clickInfo(){
        buttonHomeInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, info.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void clickMyProfile(){
        buttonHomeMyProfile.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                userPost = true;
                //getCurrentLocation();
                getCurrentLocation();
            }
        });
    }

    private void getCurrentLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(Home.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                if (isGPSEnabled()) {

                    LocationServices.getFusedLocationProviderClient(Home.this)
                            .requestLocationUpdates(locationRequest, new LocationCallback() {
                                @Override
                                public void onLocationResult(@NonNull LocationResult locationResult) {
                                    super.onLocationResult(locationResult);

                                    LocationServices.getFusedLocationProviderClient(Home.this)
                                            .removeLocationUpdates(this);

                                    if (locationResult != null && locationResult.getLocations().size() >0){

                                        int index = locationResult.getLocations().size() - 1;
                                        Location.latitude = locationResult.getLocations().get(index).getLatitude();
                                        Location.longitude = locationResult.getLocations().get(index).getLongitude();
                                        Toast.makeText(Home.this, "Location Found @" + Location.latitude + ", " + Location.longitude, Toast.LENGTH_SHORT).show();
                                        //Switch to the post page
                                        Intent intent = new Intent(Home.this, MyLocationPost.class);

                                        //Bundle that will tell the activity if the user wants to see their own posts or if they want to see other peoples posts
                                        Bundle b = new Bundle();
                                        b.putBoolean("UserPosts",userPost);
                                        intent.putExtras(b);


                                        startActivity(intent);
                                        finish();
                                        //AddressText.setText(coords[0] + "  " + coords[1]);



                                    }
                                }
                            }, Looper.getMainLooper());

                } else {
                    turnOnGPS();
                }

            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){

                if (isGPSEnabled()) {

                    getCurrentLocation();

                }else {

                    turnOnGPS();
                }
            }
        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {

                getCurrentLocation();
            }
        }
    }


    private void turnOnGPS() {
        System.out.println("GPS------------------");
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(getApplicationContext())
                .checkLocationSettings(builder.build());

        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {

                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    Toast.makeText(Home.this, "GPS is already tured on", Toast.LENGTH_SHORT).show();

                } catch (ApiException e) {

                    switch (e.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                            try {
                                ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                                resolvableApiException.startResolutionForResult(Home.this, 2);
                            } catch (IntentSender.SendIntentException ex) {
                                ex.printStackTrace();
                            }
                            break;

                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            //Device does not have location
                            break;
                    }
                }
            }
        });

    }

    private boolean isGPSEnabled() {
        LocationManager locationManager = null;
        boolean isEnabled = false;

        if (locationManager == null) {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        }

        isEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isEnabled;

    }



    private void createElements(){
        buttonHomeLogout = findViewById(R.id.buttonHomeLogout);
        buttonHomeMyLocation = findViewById(R.id.buttonHomeMyLocation);
        buttonHomeInfo = findViewById(R.id.buttonHomeInfo);
        progressBar = findViewById(R.id.progressBarHome);
        buttonHomeNewLocation = findViewById(R.id.buttonHomeNewLocation);
        buttonHomeSettings = findViewById(R.id.buttonHomeSettings);
        buttonHomeMyProfile = findViewById(R.id.buttonHomeMyProfile);
        locationRequest = LocationRequest.create();
        //get an accurate location
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);
    }
}