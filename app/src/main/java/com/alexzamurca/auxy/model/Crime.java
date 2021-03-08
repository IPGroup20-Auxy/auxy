package com.alexzamurca.auxy.model;

public class Crime {
    private final String category;
    private final Double lat;
    private final Double lng;

    /**
     * Crime data model
     * @param category of crime
     * @param lat latitude
     * @param lng longitude
     */
    public Crime(String category, Double lat, Double lng) {
        this.category = category;
        this.lat = lat;
        this.lng = lng;
    }

    public String getCategory() {
        return category;
    }

    public Double getLat() {
        return lat;
    }

    public Double getLng() {
        return lng;
    }

    @Override
    public String toString(){
        return getCategory() + getLng() + getLat();
    }
}
