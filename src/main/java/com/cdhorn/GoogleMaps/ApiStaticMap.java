package com.cdhorn.GoogleMaps;


public class ApiStaticMap {

    private String staticMapUrl = "https://maps.googleapis.com/maps/api/staticmap?zoom=10&amp;size=300x300";
    private String startMarkerOrigin; //use latitude from directions response
    private String finishMarkerDestination; //use longitude from directions response
    private String overviewPolyline; //use overview polyline from directions response
    private String startMarkerParameters = "&amp;markers=size:mid%7Ccolor:green%7Clabel:S%7C";
    private String finishMarkerParameters = "&amp;markers=size:mid%7Ccolor:red%7Clabel:F%7C";
    private String pathParamters = "&amp;path=color:blue%7Cenc:";
    private String staticMapApiKey = "&amp;key=";

    private ApiDirections apiDirections;

}
