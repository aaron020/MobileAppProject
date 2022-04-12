package com.example.mobileapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.mobileapp.databinding.ActivityMockLocationMapBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.io.IOException;
import java.util.List;

public class MockLocationMap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMockLocationMapBinding binding;
    private Button buttonSaveLocation;
    private EditText editTextSavedLocationTitle;
    private FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    private FloatingActionButton FAButtonCancelMockLM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMockLocationMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.Mockmap);
        mapFragment.getMapAsync(this);
    }

    private void mapClick(){
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                mMap.clear();
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,8));
                mMap.addMarker(new MarkerOptions().position(latLng));
                buttonSaveLocation.setVisibility(View.VISIBLE);
                editTextSavedLocationTitle.setVisibility(View.VISIBLE);
                Location.savedLatitude = latLng.latitude;
                Location.savedLongitude = latLng.longitude;
                editTextSavedLocationTitle.setText(geoCoding());
            }
        });
    }

    private String geoCoding(){
        Geocoder geoCoder = new Geocoder(this);
        List<Address> matches = null;
        try {
            matches = geoCoder.getFromLocation(Location.savedLatitude, Location.savedLongitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(matches.isEmpty()){
            return "Somewhere";
        }
        Address bestMatch = matches.get(0);

        return bestMatch.getAddressLine(0);


    }

    private void savedLocation(){
        buttonSaveLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(editTextSavedLocationTitle.getText())){
                    Toast.makeText(MockLocationMap.this,"Please give the selected location a brief title!", Toast.LENGTH_SHORT).show();
                }else{
                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    addToDb(userId,editTextSavedLocationTitle.getText().toString());

                    Intent i = new Intent(MockLocationMap.this, SaveLocation.class);
                    startActivity(i);
                }

            }
        });
    }


    public void addToDb(String userId,String title){
        DocumentReference dr = fStore.getInstance().collection("locations").document();
        savedLocation savedLocation = new savedLocation(Location.savedLongitude,Location.savedLatitude,title,userId);
        fStore.collection("locations").document().set(savedLocation).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(MockLocationMap.this, "Saved!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void FAButtonClicked(){
        FAButtonCancelMockLM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MockLocationMap.this, SaveLocation.class);
                startActivity(intent);
                finish();
            }
        });
    }




    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng myLocation = new LatLng(52.6615, -8.62);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation,6));
        mapClick();
        buttonSaveLocation = findViewById(R.id.buttonSaveLocation);
        editTextSavedLocationTitle = findViewById(R.id.editTextSavedLocationTitle);
        FAButtonCancelMockLM = findViewById(R.id.FAButtonCancelMockLM);
        savedLocation();
        FAButtonClicked();

    }
}