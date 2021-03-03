package com.alexzamurca.auxy.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.alexzamurca.auxy.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Executor;

import static android.content.ContentValues.TAG;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    // variable to check whether we are tracking locations
    boolean updateOn = false;

    // locations request is a config file for all settings related to FusedLocationProvider
    LocationRequest locationRequest;


    // Google's API for location services.
    FusedLocationProviderClient fusedLocationProviderClient;

    // Google Map object, we are going to be interacting with to build the map
    private GoogleMap mMap;

    private LatLng myLocation;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        return rootView;

    }// end of onCreate method

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * I just add a marker near Sydney, Australia.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;


        // set the type of map.
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);


        // set up all properties of locationRequest
        locationRequest = new LocationRequest();

        locationRequest.setInterval(1000 * 30);

        locationRequest.setFastestInterval(1000 * 5 );

        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        //Dan's bit
        //each polygon needs LatLng for each corner and a type to set what kind of area and so appearance
        //so provide array of arrays of LatLng for each type maybe
        ArrayList<ArrayList<LatLng>> dangerInput = new ArrayList();
        ArrayList<LatLng> temp = new ArrayList<LatLng>(Arrays.asList(new LatLng[]{new LatLng(51.380001f, -2.36f), new LatLng(51.380005f, -2.37f), new LatLng(51.380505f, -2.36f)}));
        dangerInput.add(temp);

        ArrayList<Polygon> dangerZones = new ArrayList<>();
        for (ArrayList area : dangerInput) {
            dangerZones.add(mMap.addPolygon(new PolygonOptions().addAll(area).fillColor(0x8899ff00)));
        }

        updateGPS();
    }


    private void updateGPS(){
        // get permission for the user
        // get the current location form the fusedLocatoion sercives

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        if  (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
        == PackageManager.PERMISSION_GRANTED){
            // user provided the permission

            fusedLocationProviderClient.getLastLocation().addOnSuccessListener( getActivity(), location -> {
                // we got permission get lat and longt

                myLocation = new LatLng(location.getLatitude(), location.getLongitude());
//                mMap.addMarker(new MarkerOptions()
//                                .position(myLocation));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 17.0f));

            });
            // Enables google's button which sets the camera to user's location
            mMap.setMyLocationEnabled(true);
        }else{
            requestLocationPermission();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case 2:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    updateGPS();
                }else{
                    Toast.makeText(getContext(),"This permission is required to unlocks important features", Toast.LENGTH_SHORT).show();
                }
                break;
        }


    }

    // Request users Fine Location permission
    private void requestLocationPermission()
    {
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION},2);
    }
}

