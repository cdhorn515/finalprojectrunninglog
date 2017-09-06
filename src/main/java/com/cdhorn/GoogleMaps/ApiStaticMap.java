package com.cdhorn.GoogleMaps;


public class ApiStaticMap {

    private String staticMapUrl = "https://maps.googleapis.com/maps/api/staticmap?zoom=10&size=100x100";
    private String startMarkerOrigin; //use latitude from directions response
    private String finishMarkerDestination; //use longitude from directions response
    private String overviewPolyline; //use overview polyline from directions response
    private String startMarkerParameters = "&markers=size:mid%7Ccolor:green%7Clabel:S%7C";
    private String finishMarkerParameters = "&markers=size:mid%7Ccolor:red%7Clabel:F%7C";
    private String pathParameters = "&path=color:blue%7Cenc:";
    private String staticMapApiKey = "&key=";

    private ApiDirections apiDirections;

    public String getStaticMapUrl() {
        return staticMapUrl;
    }

    public String getStartMarkerOrigin() {
        return startMarkerOrigin;
    }

    public String getFinishMarkerDestination() {
        return finishMarkerDestination;
    }

    public String getOverviewPolyline() {
        return overviewPolyline;
    }

    public String getStartMarkerParameters() {
        return startMarkerParameters;
    }

    public String getFinishMarkerParameters() {
        return finishMarkerParameters;
    }

    public String getPathParameters() {
        return pathParameters;
    }

    public String getStaticMapApiKey() {
        return staticMapApiKey;
    }

    public ApiDirections getApiDirections() {
        return apiDirections;
    }
}
