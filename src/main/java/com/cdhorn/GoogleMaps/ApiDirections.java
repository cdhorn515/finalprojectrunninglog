package com.cdhorn.GoogleMaps;

public class ApiDirections{

    private String start_address;

    private double latitude;

    private double longitude;

    private String end_address;


    private String directionsMapUrl = "https://maps.googleapis.com/maps/api/directions/json?";
    private String origin="origin="; //find by id(1)
    private String destination = "&destination="; //find by id (length of waypoints)
    private String waypoints = "&waypoints=";
    private String waypointsInfo; //user input saved to db, loop over all but first and last and concat with %7C
    private String directionsApiKey = "&key=";
    private String mode = "&mode=walking";

    public String getStart_address() {
        return start_address;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getEnd_address() {
        return end_address;
    }

    public String getDirectionsMapUrl() {
        return directionsMapUrl;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public String getWaypoints() {
        return waypoints;
    }

    public String getWaypointsInfo() {
        return waypointsInfo;
    }

    public String getDirectionsApiKey() {
        return directionsApiKey;
    }

    public String getMode() {
        return mode;
    }
}
