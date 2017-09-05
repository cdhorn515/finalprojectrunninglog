package com.cdhorn.GoogleMaps;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

import java.util.LinkedHashMap;
import java.util.List;


public class Routes extends GenericJson {

    @Key("overview_polyline")
    private LinkedHashMap<Long, OverviewPolylineObject> overviewPolylineObject;

    @Key("legs")
    private List<Legs> legs;

    public LinkedHashMap<Long, OverviewPolylineObject> getOverviewPolylineObject() {
        return overviewPolylineObject;
    }

    public List<Legs> getLegs() {
        return legs;
    }


}
