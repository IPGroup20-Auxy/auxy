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

    protected String prepend;
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
     * Customised URL constructor
     * @param context Context
     * @param year Year of interest
     * @param month Month of interest
     * @param lat Latitude
     * @param lng Longitude
     */
    public PoliceAPI(Context context, String year, String month, Double lat, Double lng) {
        this.prepend = "https://data.police.uk/api/crimes-street/all-crime?";
        this.createURL(year, month, lat, lng);
        this.context = context;
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
     * @return ArrayList<Crime></>
     */
    public ArrayList<Crime> getResponse(){

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
        return crimes;
    }

    public void onGotResponse(ArrayList<Crime> response){
        Log.d("Police API", response.toString());
    }
}
