package com.cdhorn.GoogleMaps;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

import java.util.List;

public class ApiDirectionsFeed extends GenericJson{

//    @Key("routes")
//    private LinkedHashMap<OverviewPolylineObject, String> overviewPolylineObject;
//
//    public LinkedHashMap<OverviewPolylineObject, String> getOverviewPolylineObject() {
//        return overviewPolylineObject;
//    }

    @Key("routes")
    private List<Routes> routes;

    public List<Routes> getRoutes() {
        return routes;
    }

    //    private List<ApiDirections> directions;
//
//    public List<ApiDirections> getDirections() {
//        return directions;
//    }
}
