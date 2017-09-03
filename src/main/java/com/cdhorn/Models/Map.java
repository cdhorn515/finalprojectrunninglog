package com.cdhorn.Models;

import javax.persistence.*;

@Entity
public class Map {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private boolean isPublic = false;
    private String startPosition;
    private String endPosition;

    //These should go in separate classes
    private String staticMapUrl = "https://maps.googleapis.com/maps/api/staticmap?zoom=10&amp;size=300x300";
    private String startMarkerOrigin; //use latitude from directions response
    private String finishMarkerDestination; //use longitude from directions response
    private String overviewPolyline; //use overview polyline from directions response
    private String startMarkerParameters = "&amp;markers=size:mid%7Ccolor:green%7Clabel:S%7C";
    private String finishMarkerParameters = "&amp;markers=size:mid%7Ccolor:red%7Clabel:F%7C";
    private String pathParamters = "&amp;path=color:blue%7Cenc:";
    private String staticMapApiKey = "&amp;key=";

    private String directionsMapUrl = "https://maps.googleapis.com/maps/api/directions/json?";
    private String origin="origin="; //find by id(1)
    private String destination = "&destination="; //find by id (length of waypoints)
    private String waypoints = "&waypoints=";
    private String waypointsInfo; //user input saved to db, loop over all but first and last and concat with %7C
    private String directionsApiKey = "&key=";
    private String mode = "&mode=walking";

    private String geoCodingUrl = "https://maps.googleapis.com/maps/api/geocode/json?address=";
    private String geoCodeStartAddress;
    private String geoCodeDestinationAddress;
    private String geoCodingKey = "&key=";

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne
    @JoinColumn(name = "run_id")
    private Run run;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public String getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(String startPosition) {
        this.startPosition = startPosition;
    }

    public String getEndPosition() {
        return endPosition;
    }

    public void setEndPosition(String endPosition) {
        this.endPosition = endPosition;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Run getRun() {
        return run;
    }

    public void setRun(Run run) {
        this.run = run;
    }
}
