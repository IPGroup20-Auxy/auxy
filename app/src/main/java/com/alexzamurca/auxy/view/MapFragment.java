package com.alexzamurca.auxy.view;


import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alexzamurca.auxy.R;
import com.alexzamurca.auxy.controller.TierChooser;
import com.alexzamurca.auxy.model.Crime;
import com.alexzamurca.auxy.model.PoliceAPI;

import com.alexzamurca.auxy.model.auxyFile;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import static android.content.ContentValues.TAG;

//may have repeats

public class MapFragment extends Fragment implements OnMapReadyCallback, PoliceAPI.RequestListener {

    private int radiusOfCircleOverlap = 50;
    private Set<LatLng> created_circles = new HashSet<>();
    // variable to check whether we are tracking locations
    boolean updateOn = false;

    // locations request is a config file for all settings related to FusedLocationProvider
    LocationRequest locationRequest;

    // Google's API for location services.
    FusedLocationProviderClient fusedLocationProviderClient;

    // Google Map object, we are going to be interacting with to build the map
    private GoogleMap mMap;

    private LatLng myLocation;

    private HashMap<LatLng, Integer> locationConcentrationMap;

    ArrayList<Circle> tier0Circles = null;
    ArrayList<Circle> tier1Circles = null;
    ArrayList<Circle> tier2Circles = null;

    LocationCallback locationCallback;

    // calling the Auxy files class
    auxyFile aF = new auxyFile();

    public ArrayList<Circle> drawCircleTierFixedRadius(GoogleMap googleMap, ArrayList<LatLng> centres, int radius, String tier){
        boolean colourBlindState = SettingsFragment.colourBlindState;
        Log.d("Blind", String.valueOf(colourBlindState));

        int fill;
        int border;
        
        if (!colourBlindState) {
            switch (tier) {
                case "tier2":
                    fill = MainActivity.context.getResources().getColor(R.color.tier2_colour);
                    break;
                case "tier1":
                    fill = MainActivity.context.getResources().getColor(R.color.tier1_colour);
                    break;
                case "tier0":
                    fill = MainActivity.context.getResources().getColor(R.color.tier0_colour);//change green
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + tier);
            }
            ArrayList<Circle> dangerZones = new ArrayList<Circle>();
            for (LatLng centre : centres) {
                if (!created_circles.contains(centre)) {
                    dangerZones.add(mMap.addCircle(new CircleOptions().center(centre).fillColor(fill).radius(radius).strokeWidth(0)));
                    created_circles.add(centre);
                    Log.d(TAG, "drawCircleTierFixedRadius: set created_circles: " + created_circles);
                }
            }
            return dangerZones;
        }
        else{
            switch (tier) {
                case "tier0":
                    fill = MainActivity.context.getResources().getColor(R.color.tier0_colourBlind);
                    break;
                case "tier1":
                    fill = MainActivity.context.getResources().getColor(R.color.tier1_colourBlind);
                    break;
                case "tier2":
                    fill = MainActivity.context.getResources().getColor(R.color.tier2_colourBlind);//change green
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + tier);
            }
            ArrayList<Circle> dangerZones = new ArrayList<Circle>();
            for (LatLng centre : centres) {
                if (!created_circles.contains(centre)) {
                    dangerZones.add(mMap.addCircle(new CircleOptions().center(centre).fillColor(fill).radius(radius).strokeWidth(0)));
                    created_circles.add(centre);
                    Log.d(TAG, "drawCircleTierFixedRadius: set created_circles: " + created_circles);
                }
            }
            return dangerZones;
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);


        // Initialise the hash map
        locationConcentrationMap = new HashMap<>();

        Log.d(TAG, "onCreateView: hashmap reinitialised, hashmap length: " + locationConcentrationMap.keySet().size());

        return rootView;
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

        final float[] currentZoom = {mMap.getCameraPosition().zoom};
        final LatLng[] currentPos = {mMap.getProjection().getVisibleRegion().nearRight};
        // set the type of map.
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        // Add a marker in Sydney and move the camera
        // COMMENTED OUT: LatLng Bath = new LatLng(51.380001f, -2.36f);



        // set up all properties of locationRequest
        locationRequest = new LocationRequest();

        locationRequest.setInterval(1000 * 20);

        locationRequest.setFastestInterval(1000 * 5);

        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        // this event is triggered whenever the update interval is met.
        locationCallback = new LocationCallback() {

            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                // save the location
                Location location = locationResult.getLastLocation();
                // update the my location pointer
                LatLng new_pos = new LatLng(location.getLatitude(), location.getLongitude());
//                check if user has significant change in location
                if ( Math.abs(myLocation.latitude - new_pos.latitude) > 0.001f
                        || Math.abs(myLocation.longitude - new_pos.longitude) > 0.001f ){
                    myLocation = new_pos;
                    // write to File "current_location"
                    aF.aWrite(myLocation.toString());

                    Log.d(TAG, "onLocationResult: Camera pos Changed Update_heat");
                }
            }
        };

        mMap.setOnCameraIdleListener( new GoogleMap.OnCameraIdleListener(){
            @Override
            public void onCameraIdle() {
                // If the zoom level is too big we dont draw the circles
                if (mMap.getCameraPosition().zoom > 16f){
                    // police pi request
                    if ( currentZoom[0] != mMap.getCameraPosition().zoom){
                        currentZoom[0] = mMap.getCameraPosition().zoom;
                        update_heat();
                        Log.d(TAG, "onLocationResult: Camera Zoom Changed Update_heat");
                    }
                    // checks if the camera position has changed
                    LatLng new_pos = mMap.getProjection().getVisibleRegion().nearRight;
                    if ( currentPos[0] != new_pos){
                        currentPos[0] = new_pos;
                        update_heat();
                        Log.d(TAG, "onLocationResult: Camera pos Changed Update_heat");
                    }
                }
            }
        });


        updateGPS();

    } // end of onMapReady method

    public void update_heat(){
        // Police api send request
        PoliceAPI papi = new PoliceAPI(this, getActivity().getApplicationContext(), getBoundsLatLng());
        Log.d(TAG, "startLocationUpdates: getBoundsLatLng: ok Called " );
        papi.getResponse(); // Response not used
    }

    private void startLocationUpdates() {

        if (ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // here to request the missing permissions, and then overriding
            requestLocationPermission();
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);

    }


    private void updateGPS(){
        // get permission for the user
        // get the current location form the fusedLocation services

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        if  (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
        == PackageManager.PERMISSION_GRANTED){
            // user provided the permission

            fusedLocationProviderClient.getLastLocation().addOnSuccessListener( requireActivity(), location -> {
                // we got permission get lat and longt
                myLocation = new LatLng(location.getLatitude(), location.getLongitude());
                // write to File "current_location"
                aF.aWrite(myLocation.toString());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 17.0f));
                startLocationUpdates();

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
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    updateGPS();
                }else{
                    Log.d(TAG, "This permission is required to unlocks important features");
                    // Issue: Toast is not showing
//                    Toast.makeText(getContext(),"This permission is required to unlocks important features", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    @Override
    public void onGotResponse(ArrayList<Crime> response) {
        Log.d("Map fragment API response", response.toString());
        
        try{
            for (Crime c : response){
                LatLng location = new LatLng(c.getLat(), c.getLng());
                if (!locationConcentrationMap.containsKey(location)){
                    locationConcentrationMap.put(location, 1);
                } else {
                    locationConcentrationMap.put(location, locationConcentrationMap.get(location) + 1);
                }
            }
        } catch (NullPointerException e){
            e.printStackTrace();
        }

        Log.d(TAG, "onGotResponse: Length of HashMap: " + locationConcentrationMap.keySet().size());

        // Initialise the variables for drawing
        ArrayList<LatLng> tier0 = new ArrayList<>();
        ArrayList<LatLng> tier1 = new ArrayList<>();
        ArrayList<LatLng> tier2 = new ArrayList<>();
        
        // After location concentration map is formed
        for(LatLng location : locationConcentrationMap.keySet())
        {
            TierChooser tierChooser = new TierChooser();
            int tier = tierChooser.getTier(locationConcentrationMap.get(location));
            Log.d(TAG, "onGotResponse: Number of Crimes: " + locationConcentrationMap.get(location));
            Log.d(TAG, "onGotResponse: Picked tier: " + tier);
            switch (tier)
            {
                case 0:
                    tier0.add(location);

                    break;

                case 1:
                    tier1.add(location);

                    break;

                case 2:
                    tier2.add(location);

                    break;
            }
        }


        tier0Circles = drawCircleTierFixedRadius(mMap,tier0, radiusOfCircleOverlap, "tier0");
        tier1Circles = drawCircleTierFixedRadius(mMap,tier1, radiusOfCircleOverlap, "tier1");
        tier2Circles = drawCircleTierFixedRadius(mMap,tier2, radiusOfCircleOverlap, "tier2");

        // Reset hashmap
        locationConcentrationMap = new HashMap<>();
    }


    // Request users Fine Location permission
    private void requestLocationPermission()
    {
        requestPermissions( new String[]{Manifest.permission.ACCESS_FINE_LOCATION},2);
    }

    public ArrayList<LatLng> getBoundsLatLng(){

        // get the center of the area
        LatLngBounds bounds = mMap.getProjection().getVisibleRegion().latLngBounds;
        ArrayList<LatLng> corners = new ArrayList<>();
        /*
        corners.add(new LatLng(51.395284, -2.377752));
        corners.add(new LatLng(51.395284, -2.346596));
        corners.add(new LatLng(51.378573, -2.377752));
        corners.add(new LatLng(51.378573, -2.346596));
        */

        corners.add(new LatLng(bounds.southwest.latitude, bounds.northeast.longitude));
        corners.add(bounds.northeast);
        corners.add(new LatLng(bounds.northeast.latitude, bounds.southwest.longitude));
        corners.add(bounds.southwest);



        Log.d(TAG, "getBoundsLatLng: NearLeft: (" + bounds.southwest.latitude + ", " +  bounds.northeast.longitude);
        Log.d(TAG, "getBoundsLatLng: FarLeft: (" + bounds.northeast.latitude + ", " + bounds.northeast.longitude);
        Log.d(TAG, "getBoundsLatLng: NearRight: (" + bounds.northeast.latitude + ", " + bounds.southwest.longitude);
        Log.d(TAG, "getBoundsLatLng: FarRight: (" + bounds.southwest.latitude + ", " + bounds.southwest.latitude);

        return corners;
    }


}

