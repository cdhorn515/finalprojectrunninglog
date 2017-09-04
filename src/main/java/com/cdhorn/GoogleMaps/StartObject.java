package com.cdhorn.GoogleMaps;


import com.google.api.client.util.Key;

public class StartObject {

    @Key("lat")
    private double startLat;
    @Key("lng")
    private double startLng;

    public double getStartLat() {
        return startLat;
    }

    public double getStartLng() {
        return startLng;
    }
}
