package com.cdhorn.GoogleMaps;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;


public class Routes extends GenericJson {

    @Key("overview_polyline")
    private OverviewPolylineObject overviewPolylineObject;

//    @Key("legs")
//    private List<Legs> legs;

    public OverviewPolylineObject getOverviewPolylineObject() {
        return overviewPolylineObject;
    }

//    public List<Legs> getLegs() {
//        return legs;
//    }
}
