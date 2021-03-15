package com.alexzamurca.auxy.view;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import android.content.res.Resources;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.alexzamurca.auxy.R;

import com.alexzamurca.auxy.model.Crime;
import com.alexzamurca.auxy.model.PoliceAPI;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import com.alexzamurca.auxy.model.Crime;
import com.alexzamurca.auxy.model.PoliceAPI;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

//may have repeats
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.HashMap;


import com.google.android.gms.tasks.OnSuccessListener;

import java.lang.reflect.GenericArrayType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Executor;

import static android.content.ContentValues.TAG;

import static android.content.ContentValues.TAG;

public class MapFragment extends Fragment implements OnMapReadyCallback, PoliceAPI.RequestListener{

    private NavController navController;

    // variable to check whether we are tracking locations
    boolean updateOn = false;

    // locations request is a config file for all settings related to FusedLocationProvider
    LocationRequest locationRequest;


    // Google's API for location services.
    FusedLocationProviderClient fusedLocationProviderClient;

    // Google Map object, we are going to be interacting with to build the map
    private GoogleMap mMap;
    private ArrayList<Crime> PoliceAPIResponse;

    private LatLng myLocation;

    private HashMap<String, Integer> locationConcentrationMap;


    //private GoogleMap mMap;

    public ArrayList<Circle> drawCircleTierFixedRadius(GoogleMap googleMap, ArrayList<LatLng> centres, int radius, String tier){

        int fill;
        int border;
        switch (tier){
            case "tier2":
                fill=0x88FF0000;
                break;
            case "tier1":
                fill=0x88FF8C00;
                break;
            case "tier0":
                fill=0x88FFFF00;//change green
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + tier);
        }
        ArrayList<Circle> dangerZones =new ArrayList<Circle>();
        for(LatLng centre: centres){
            dangerZones.add(googleMap.addCircle(new CircleOptions() .center(centre).fillColor(fill).radius(radius).strokeWidth(0)));
        }
        return dangerZones;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * I just add a marker near Sydney, Australia.
     * draws circles with appearance matching tier on the map
     * @param googleMap the map the circles are drawn on
     * @param centres each item represents the centre of a circle and will be paired with the same index of radii
     * @param radii each item represents the radius of a circle and will be paired with the same index of centres
     * @param tier indicates whether the circles should be drawn in red, orange or yellow
     * @return ArrayList of Circle objects
     */
    public ArrayList<Circle> drawCircleTier(GoogleMap googleMap, ArrayList<LatLng> centres, ArrayList<Integer> radii, String tier){
        if (centres.size()!=radii.size()){
            throw new IllegalStateException("Centres and radii must be same length");
        }// other error checking?
        int fill;
        int border;
        switch (tier){
            case "red":
                fill=0x88FF0000;
                break;
            case "orange":
                fill=0x88FF8C00;
                break;
            case "yellow":
                fill=0x88FFFF00;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + tier);
        }
        ArrayList<Circle> dangerZones =new ArrayList<Circle>();

        Iterator<LatLng> it1 = centres.iterator();
        Iterator<Integer> it2 = radii.iterator();
        while (it1.hasNext() && it2.hasNext()){
            dangerZones.add(googleMap.addCircle(new CircleOptions() .center(it1.next()).fillColor(fill).radius(it2.next()).strokeWidth(0)));
        }
        return dangerZones;
    }


    /**
     * draws polygons of appearance matching tier on to the map
     * @param googleMap the map the polygons are drawn on
     * @param input each item within is an ArrayList of LatLngs where each LatLng is a vertex of a polygon
     * @param tier indicates whether the polygons should be drawn in red, orange or yellow
     * @return returns the polygons drawn on the map, should further manipulation be required.
     */
    public ArrayList<Polygon> drawPolygonTier(GoogleMap googleMap, ArrayList<ArrayList<LatLng>> input, String tier){
        // add tests? invalid tier covered and other stuff type should handle
        int fill;
        int border;
        switch (tier){
            case "red":
                fill=0x88FF0000;
                break;
            case "orange":
                fill=0x88FF8C00;
                break;
            case "yellow":
                fill=0x88FFFF00;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + tier);
        }

        ArrayList<Polygon> dangerZones = new ArrayList<>();
        for (ArrayList area : input){
            dangerZones.add(googleMap.addPolygon(new PolygonOptions() .addAll(area).fillColor(fill).strokeWidth(0)));
        }
        for (Polygon polygon:dangerZones){
            polygon.setTag(tier);
        }
        System.out.println(dangerZones.get(0).getTag());
        return dangerZones;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;


        // set the type of map.
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        // Add a marker in Sydney and move the camera
        // COMMENTED OUT: LatLng Bath = new LatLng(51.380001f, -2.36f);



        // set up all properties of locationRequest
        locationRequest = new LocationRequest();

        locationRequest.setInterval(1000 * 30);

        locationRequest.setFastestInterval(1000 * 5 );

        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        //Dan's bit
        //each polygon needs LatLng for each corner and a type to set what kind of area and so appearance
        //so provide array of arrays of LatLng for each type maybe
        int RADIUS;
        ArrayList<LatLng> tier0;
        ArrayList<LatLng> tier1;
        ArrayList<LatLng> tier2;


        ArrayList<Circle> tier0Circles = drawCircleTierFixedRadius(mMap,tier0, RADIUS, "tier0");
        ArrayList<Circle> tier1Circles = drawCircleTierFixedRadius(mMap,tier1, RADIUS, "tier1");
        ArrayList<Circle> tier2Circles = drawCircleTierFixedRadius(mMap,tier2, RADIUS, "tier2");

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

                // Get JSONArray from Police API
                PoliceAPI papi = new PoliceAPI(this, getActivity().getApplicationContext(), "https://data.police.uk/api/crimes-street/all-crime?lat=51.37973&lng=-2.32656&date=2019-01");
                papi.getResponse(); // Response not used
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

        Log.d(TAG, "onRequestPermissionsResult: ");
        
        switch (requestCode) {
            case 2:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Reload fragment
                    Log.d(TAG, "onRequestPermissionsResult: Need to reload fragment");
                    navController.navigate(R.id.action_mapFragment_self);
                } else {
                    Toast.makeText(getContext(), "This permission is required to unlocks important features", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    @Override
    public void onGotResponse(ArrayList<Crime> response) {
        Log.d("Map fragment API response", response.toString());
        try{
            for (Crime c : response){
                String locationString = c.getLat().toString() + ',' + c.getLng().toString();
                if (!locationConcentrationMap.containsKey(locationString)){
                    locationConcentrationMap.put(locationString, 1);
                } else {
                    locationConcentrationMap.put(locationString, locationConcentrationMap.get(locationString) + 1);
                }
            }
        } catch (NullPointerException e){
            e.printStackTrace();
        }

    }
    // Request users Fine Location permission
    private void requestLocationPermission()
    {
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION},2);
    }


}

