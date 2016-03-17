package com.example.owner.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.HashMap;

public class MapsActivity extends FragmentActivity implements GoogleMap.OnMyLocationButtonClickListener,
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback, GoogleMap.OnPolygonClickListener {

    private static final String FIREBASE_URL = "https://smartpark1.firebaseio.com";

    private GoogleMap mMap;
    long[][][] parkSystem;
    long[] noZones;
    long noParks;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Firebase.setAndroidContext(this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Firebase parkArray = new Firebase(FIREBASE_URL);
        noParks = 1;

        parkArray.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("ESTACIONAMIENTO: " + dataSnapshot.getValue());
                //noParks = dataSnapshot.getChildrenCount();

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        noZones = new long[(int)noParks];
        Firebase[] park = new Firebase[(int)noParks];
        for (int i = 0; i < noParks; i++){
            String temp = "Estacionamiento"+i;
            final int finalI = i;
            park[i] = new Firebase(FIREBASE_URL).child(temp);

            park[i].addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    noZones[finalI] = dataSnapshot.getChildrenCount();
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
        }
        //orientado a objetos pendiente!!
        parkSystem = new long[(int)noParks][2][2];

        //implementar iteracion con json corregido Zona0 no ZonaA
        Firebase carLimit = new Firebase(FIREBASE_URL).child("Estacionamiento1/Zona0/carLimit");
        Firebase currentCars = new Firebase(FIREBASE_URL).child("Estacionamiento1/Zona0/currentCars");
        System.out.println("noParks "+noParks+"");
        carLimit.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                parkSystem[0][0][0] = (long) dataSnapshot.getValue();
                long i = (long) dataSnapshot.getValue();
                System.out.println("geting carlimit: "+i);

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        currentCars.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                parkSystem[0][0][1] = (long) dataSnapshot.getValue();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        Firebase carLimit2 = new Firebase(FIREBASE_URL).child("Estacionamiento1/Zona1/carLimit");
        Firebase currentCars2 = new Firebase(FIREBASE_URL).child("Estacionamiento1/Zona1/currentCars");
        //for(int i = )


        carLimit2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                parkSystem[0][1][0] = (long) dataSnapshot.getValue();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        currentCars2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                parkSystem[0][1][1] = (long) dataSnapshot.getValue();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng park1 = new LatLng(20.674303, -103.463448);

        //mMap.addMarker(new MarkerOptions().position(park1).title("Parking   1").alpha(0.5f));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(park1, 16));

        //17
        mMap.isMyLocationEnabled();


        GroundOverlayOptions parkingMap = new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromResource(R.drawable.maprot))
                .position(park1, 180f, 80f);

        GroundOverlay imageOverlay = googleMap.addGroundOverlay(parkingMap);




        //implementar en iteracion

        Polygon polygon = googleMap.addPolygon(new PolygonOptions()
                .add(new LatLng(20.674654, -103.464296),
                        new LatLng(20.674584, -103.463459),
                        new LatLng(20.674027, -103.463480),
                        new LatLng(20.674087, -103.464317))
                //.fillColor(evalColor(parkSystem[0][0][0], parkSystem[0][0][1]))
                        .fillColor(0x3F0000FF)
                .strokeWidth(0f));

        polygon.setClickable(true);
        polygon.setZIndex(0);

        Polygon polygon1 = googleMap.addPolygon(new PolygonOptions()
                .add(new LatLng(20.674584, -103.463330),
                        new LatLng(20.674589, -103.463113),
                        new LatLng(20.674356, -103.462531),
                        new LatLng(20.673952, -103.462577),
                        new LatLng(20.674012, -103.463339))
                .fillColor(evalColor( parkSystem[0][1][0],  parkSystem[0][1][1]))
                        //.fillColor(0x3F0000FF)
                .strokeWidth(0f));

        polygon1.setClickable(true);
        polygon1.setZIndex(1);

        /*
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(20.674303, -103.463448))
                .title("ZONE " + (int) polygon1.getZIndex()));
                */
        googleMap.setOnPolygonClickListener(this);

    }
    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onPolygonClick(Polygon polygon) {
        Toast.makeText(getApplicationContext(),"Zona "+(int)polygon.getZIndex(),Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this, ZoneActivity.class);
        long maxCar = parkSystem[0][(int) polygon.getZIndex()][0],
            curCar = parkSystem[0][(int) polygon.getZIndex()][1];
        i.putExtra("values",
                new String[]{"0",
                        (int)polygon.getZIndex()+"",
                        maxCar+"",
                        curCar+"",
                        (maxCar-curCar)+""});
        startActivity(i);
    }

    public int evalColor(long maxCar, long curCar){
        int deltaCar = (int) (maxCar-curCar);
        System.out.println("maxCar "+maxCar);
        System.out.println("curCar "+curCar);
        if(deltaCar == maxCar) return 0x3F0000FF;
        else if(deltaCar < maxCar && deltaCar >= maxCar/2) return 0x3F00FF00;
        else if(deltaCar < maxCar/2 && deltaCar >= maxCar/3) return 0x3FFFFF00;
        else if(deltaCar < maxCar/3 && deltaCar >= maxCar/4) return 0x3FFFA500;
        else if(deltaCar == 0) return 0x3FFF0000;

        return 0x3FFFFFFF;
    }
    //10.43.19.20
}

