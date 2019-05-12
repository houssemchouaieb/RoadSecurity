package com.example.houssem.adminroadsecurity;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener {

    private GoogleMap mMap;
    FirebaseDatabase mDatabase;
    DatabaseReference mReferenceZones;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mDatabase=FirebaseDatabase.getInstance();
        mReferenceZones=mDatabase.getReference();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng tunisie = new LatLng(35.69, 10.74);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(tunisie,8));
        mReferenceZones.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot child : dataSnapshot.getChildren() ){
                    float lat=Float.parseFloat(child.child("latitude").getValue().toString());
                    float longi=Float.parseFloat(child.child("longitude").getValue().toString());
                    LatLng pos=new LatLng(lat,longi);
                    BitmapDescriptor img;
                    if(Integer.parseInt(child.child("score").getValue().toString())>6){
                         img=BitmapDescriptorFactory.fromResource(R.drawable.ex);
                    }
                    else{
                         img=BitmapDescriptorFactory.fromResource(R.drawable.warn);
                    }
                    mMap.addMarker(new MarkerOptions().position(pos).title("Accident").snippet("dégré: "+child.child("score").getValue()).icon(img));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View v) {

    }
}
