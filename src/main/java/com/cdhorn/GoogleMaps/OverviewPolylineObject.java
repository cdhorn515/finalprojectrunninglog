package com.cdhorn.GoogleMaps;


import com.google.api.client.util.Key;

public class OverviewPolylineObject {

    @Key("points")
    private String polyline;

    public String getPolyline() {
        return polyline;
    }
}
