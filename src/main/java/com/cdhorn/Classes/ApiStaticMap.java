package com.cdhorn.Classes;


public class ApiStaticMap {

    private String staticMapUrl = "https://maps.googleapis.com/maps/api/staticmap?zoom=14&size=500x500";
    private String startMarkerParameters = "&markers=size:mid%7Ccolor:green%7Clabel:S%7C";
    private String finishMarkerParameters = "&markers=size:mid%7Ccolor:red%7Clabel:F%7C";
    private String pathParameters = "&path=color:blue%7Cenc:";
    private String staticMapApiKey = "&key=";

    public String getStaticMapUrl() {
        return staticMapUrl;
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

}
