package com.cdhorn.GoogleMaps;


import com.google.api.client.util.Key;

public class EndObject {

    @Key("lat")
    private double endLat;

    @Key("lng")
    private double endLng;

    public double getEndLat() {
        return endLat;
    }

    public double getEndLng() {
        return endLng;
    }
}
