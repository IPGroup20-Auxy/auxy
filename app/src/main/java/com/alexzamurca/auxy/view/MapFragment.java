package com.alexzamurca.auxy.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.alexzamurca.auxy.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.ArrayList;
import java.util.Arrays;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync( this);
        return rootView;
    }
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
        // Add a marker in Sydney and move the camera
        LatLng Bath = new LatLng(51.380001f, -2.36f);
        //Dan's bit
        //each polygon needs LatLng for each corner and a type to set what kind of area and so appearance
        //so provide array of arrays of LatLng for each type maybe
        ArrayList<ArrayList<LatLng>> dangerInput=new ArrayList();
        ArrayList<LatLng> temp= new ArrayList<LatLng>(Arrays.asList(new LatLng[]{new LatLng(51.380001f, -2.36f), new LatLng(51.380005f, -2.37f), new LatLng(51.380505f, -2.36f)}));
        dangerInput.add(temp);

        ArrayList<Polygon> dangerZones = new ArrayList<>();
        for (ArrayList area : dangerInput){
            dangerZones.add(mMap.addPolygon(new PolygonOptions() .addAll(area).fillColor(0x8899ff00)));
        }



        mMap.addMarker(new MarkerOptions()
                .position(Bath)
                .title("Marker in Bath"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(Bath, 17.0f));
    }
}

