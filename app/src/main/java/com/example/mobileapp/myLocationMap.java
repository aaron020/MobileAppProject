package com.example.mobileapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.mobileapp.databinding.ActivityMyLocationMapBinding;

import java.util.ArrayList;
import java.util.HashMap;

public class myLocationMap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMyLocationMapBinding binding;
    private Bundle bundle = null;
    private Button buttonViewPost;
    private ArrayList<Post> posts;
    private Marker markerClicked;
    private String youId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityMyLocationMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng myLocation = new LatLng(Location.latitude, Location.longitude);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation,15));
        MarkerOptions markerOptions = new MarkerOptions().position(myLocation).title("You");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        Marker marker = mMap.addMarker(markerOptions);
        youId = marker.getId();
        buttonViewPost = findViewById(R.id.buttonViewPost);
        addMarkers();
        markerClick();
        viewPost();


    }

    private void markerClick(){
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                if(!marker.getId().equals(youId)){
                    buttonViewPost.setVisibility(View.VISIBLE);
                    markerClicked = marker;
                }else{
                    buttonViewPost.setVisibility(View.INVISIBLE);
                }
                return false;
            }
        });
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                buttonViewPost.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void viewPost(){
        buttonViewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(myLocationMap.this, DetailedPost.class);
                //Send the info of the post to the next activity
                Post post;
                if(markerClicked.getTag() != null){
                    post = (Post) markerClicked.getTag();
                    i.putExtra("post", post);
                    startActivity(i);
                }

            }
        });
    }

    private void addMarkers(){
        bundle = getIntent().getExtras();
        posts = new ArrayList<>();
        posts = bundle.getParcelableArrayList("posts");
        for(Post p : posts){
            addMarker(p);
        }
    }



    private void addMarker(Post post){
        LatLng location = new LatLng(post.getLatitude(), post.getLongitude());
        Marker marker = mMap.addMarker(new MarkerOptions()
                                        .position(location)
                                        .title(post.getTitle()).snippet(post.getShortDesc()));
        marker.setTag(post);
    }
}