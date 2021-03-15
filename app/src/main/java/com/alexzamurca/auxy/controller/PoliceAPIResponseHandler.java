package com.alexzamurca.auxy.controller;

import com.alexzamurca.auxy.model.PoliceAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

// From the police API we get an array back that gives us information about the crime
/*
* Here is an example response
* [
    {
        "category": "violent-crime",
        "location_type": "Force",
        "location": {
            "latitude": "52.643950",
            "street": {
                "id": 884227,
                "name": "On or near Abbey Gate"
            },
            "longitude": "-1.143042"
        },
        "context": "",
        "outcome_status": {
            "category": "Unable to prosecute suspect",
            "date": "2017-02"
        },
        "persistent_id": "4d83433f3117b3a4d2c80510c69ea188a145bd7e94f3e98924109e70333ff735",
        "id": 54726925,
        "location_subtype": "",
        "month": "2017-02"
    }
]
*
* We care about:
*  how many crimes there are in the response (increase score for the area depending on how many crimes there are)
* if the outcome is "imprisoned" then reduce the score
 */
public class PoliceAPIResponseHandler
{
    private JSONArray APIResponse;
    private int numberOfCrimesInArea;
    /*
     * A solution to finding the score is to find the average crimes per our given area (e.g. we expect an average of 100 crimes for an area of 100m^2)
     * If the number of crimes in the last month for the area, allows us to determine a score
     * being relatively close to average would mean a score of 40-70 (yellow)
     * being much lower than average we would expect a score of 0-39 (green)
     * being much higher than average we would expect a score of 71-100 (red)
     */
    private int score;

    public PoliceAPIResponseHandler(String response)
    {
        this.APIResponse = stringToJSON(response);
        this.numberOfCrimesInArea = findNumberOfCrimes();
    }

    private JSONArray stringToJSON(String response)
    {
        try
        {
            return new JSONArray(response);
        }
        catch(JSONException e)
        {
            return null;
        }
    }

    private int findNumberOfCrimes()
    {
        int counter = 0;
        if(APIResponse==null) return 0;

        for(int i = 0; i < APIResponse.length(); i++)
        {
            try
            {
                // Get the json object
                JSONObject jsonObject = APIResponse.getJSONObject(i);
                // Get the category
                String category = jsonObject.getString("category");
                // See if the category is what we care about
                boolean isRelevantCategory = isRelevantCategory(category);
                // If so add to the counter
                if(isRelevantCategory) counter++;
                // Otherwise don't do anything
            }
            catch(JSONException e)
            {
                continue;
            }
        }

        return counter;
    }

    private boolean isRelevantCategory(String category)
    {
        String[] irrelevantCategories = new String[]{"Bicycle theft", "Shoplifting", "Burglary"};
        for(String irrelevantCategory: irrelevantCategories)
        {
            if(category.equals(irrelevantCategory)) return false;
        }
        return true;
    }
}