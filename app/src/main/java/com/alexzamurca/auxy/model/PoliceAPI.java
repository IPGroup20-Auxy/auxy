package com.alexzamurca.auxy.model;

import android.content.Context;
import android.nfc.Tag;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * This class is for communicating with the police API
 * It creates a URI pointing to the JSON resource based on
 * user's location and a date and gets crime data within 1 mile radius
 */
public class PoliceAPI {

    private static final String TAG = "PoliceAPI";
    
    protected final String prepend = "https://data.police.uk/api/crimes-street/all-crime?";
    protected String URL;
    private Context context;
    protected static ArrayList<Crime> crimes = new ArrayList<>();
    private RequestListener requestListener;

    public interface RequestListener
    {
        void onGotResponse(ArrayList<Crime> response);
    }


    /**
     * Constructor using context and pre-written URL
     * @param context Context
     * @param URL JSON file locator
     */
    public PoliceAPI(RequestListener requestListener, Context context, String URL){
        this.requestListener = requestListener;
        this.context = context;
        this.URL = URL;
    }

    /**
     * Customised URL constructor for specified point
     * @param context Context
     * @param year Year of interest
     * @param month Month of interest
     * @param lat Latitude
     * @param lng Longitude
     */
    public PoliceAPI(Context context, String year, String month, Double lat, Double lng) {
        this.createURL(year, month, lat, lng);
        this.context = context;
    }

    /**
     * URL with custom area
     * @param requestListener Interface for listening for network requests
     * @param context application context
     * @param locations ArrayList of locations in LatLng
     */
    public PoliceAPI(RequestListener requestListener, Context context, ArrayList<LatLng> locations){
        Log.d(TAG, "PoliceAPI: called");
        this.requestListener = requestListener;
        this.context = context;

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < locations.size(); i++ )
        {
            LatLng currentLocation = locations.get(i);
            stringBuilder.append(currentLocation.latitude);
            stringBuilder.append(",");
            stringBuilder.append(currentLocation.longitude);
            // If not last index add semicolon
            if(i!= (locations.size()-1)) stringBuilder.append(":");
        }

        this.URL = this.prepend + "poly=" + stringBuilder.toString();
    }

    /**
     * Helper function for creating URL for specified point
     * @param year year
     * @param month month
     * @param lat latitude
     * @param lng longitude
     */
    private void createURL(String year, String month, Double lat, Double lng){
        this.URL = prepend + "lat=" + lat + "&lng=" + lng + "&date=" + year + '-' + month;
    }

    /**
     * Send GET request for json array containing crime information
     */
    public void getResponse(){

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, this.URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try{
                    for (int i = 0; i < response.length(); i++){
                        JSONObject jsonObject = response.getJSONObject(i);
                        Crime c = new Crime(jsonObject.getString("category"),
                                Double.valueOf(jsonObject.getJSONObject("location").getString("latitude")),
                                Double.valueOf(jsonObject.getJSONObject("location").getString("longitude")));
                        crimes.add(c);
                    }

                    requestListener.onGotResponse(crimes);
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        RequestQueueSingleton.getInstance(this.context).addToRequestQueue(jsonArrayRequest);
    }

    public void onGotResponse(ArrayList<Crime> response){
        Log.d("Police API", response.toString());
    }
}
