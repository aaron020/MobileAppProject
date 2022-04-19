package com.example.mobileapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.mobileapp.databinding.ActivityMyLocationMapBinding;

import java.util.ArrayList;
import java.util.HashMap;
/*
Map which displays the users current location , also displays all posts around the user
 */
public class myLocationMap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMyLocationMapBinding binding;
    private Bundle bundle = null;
    private Button buttonViewPost;
    private ArrayList<Post> posts;
    private Marker markerClicked;
    private String youId;
    //Names of each of the icons
    private String[] type = {"helpful_bm", "informative_bm", "curious_bm","event_bm","academic_bm","weather_bm","other_bm"};
    private FloatingActionButton FAButtonCancelMLM;
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
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(resizeBitmap("you_bm",175,175)));
        Marker marker = mMap.addMarker(markerOptions);
        Circle circle = mMap.addCircle(new CircleOptions()
                .center(myLocation)
                .radius(Settings.Distance * 1000)
                .strokeColor(Color.RED)
                .fillColor(Color.parseColor("#2271cce7")));
        youId = marker.getId();
        buttonViewPost = findViewById(R.id.buttonViewPost);
        FAButtonCancelMLM = findViewById(R.id.FAButtonCancelMLM);
        addMarkers();
        markerClick();
        viewPost();
        FAButtonClick();


    }


    private void FAButtonClick(){
        FAButtonCancelMLM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(myLocationMap.this, MyLocationPost.class);
                Bundle b = new Bundle();
                b.putBoolean("UserPosts",false);
                intent.putExtras(b);
                startActivity(intent);
                finish();
            }
        });
    }


    //Function from https://stackoverflow.com/questions/41509791/how-to-fix-custom-size-of-google-maps-marker-in-android
    public Bitmap resizeBitmap(String drawableName,int width, int height){
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(),getResources().getIdentifier(drawableName, "drawable", getPackageName()));
        return Bitmap.createScaledBitmap(imageBitmap, width, height, false);
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
//        Marker marker = mMap.addMarker(new MarkerOptions()
//                                        .position(location)
//                                        .title(post.getTitle()).snippet(post.getShortDesc()));


        MarkerOptions markerOptions = new MarkerOptions().position(location).title(post.getTitle()).snippet(post.getShortDesc());
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(resizeBitmap(type[post.getTypeChosen()-1],150,150)));
        Marker marker = mMap.addMarker(markerOptions);
        marker.setTag(post);
    }
}