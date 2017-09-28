package com.cdhorn.Classes;


import com.cdhorn.Interfaces.GeocodingInterface;
import com.cdhorn.Interfaces.MapRepository;
import com.cdhorn.Interfaces.RunRepository;
import com.cdhorn.Models.Map;
import com.cdhorn.Models.Run;
import feign.Feign;
import feign.gson.GsonDecoder;
import org.springframework.mobile.device.Device;

import java.sql.Time;

public class HelperFx {

    public Map getMap(MapRepository mapRepo, RunRepository runRepo, String runId, String mapId) {
        long myMapId = Long.parseLong(mapId);
        Map myMap = mapRepo.findOne(myMapId);
        long myRunId = Long.parseLong(runId);
        Run myRun = runRepo.findOne(myRunId);
        myRun.setMap(myMap);
        runRepo.save(myRun);
        return myMap;
    }

    public Map updateMap(MapRepository mapRepo, String mapId) {
        long intMapId = Integer.valueOf(mapId);
        Map updateMap = mapRepo.findOne(intMapId);
        return updateMap;
    }

    public String getPosition(String address) {

        String addressNoSpaces = address.replace(" ", "+");
        ApiKey apiKey = new ApiKey();
        GeocodingInterface geocodingInterface = Feign.builder()
                .decoder(new GsonDecoder())
                .target(GeocodingInterface.class, "https://maps.googleapis.com");
        GeocodingResponse response = geocodingInterface.geocodingResponse(addressNoSpaces + "+greenville+sc",
                apiKey.getGEOCODING_API());
        double lat = response.getResults().get(0).getGeometry().getLocation().getLat();
        double lng = response.getResults().get(0).getGeometry().getLocation().getLng();
        String position = lat + "," + lng;
        return position;
    }

    public void setRunData(String time, float distance, Run userRun, RunRepository runRepo) {
        userRun.setDistance(distance);
        Time runTime = Time.valueOf(time);
        userRun.setTime(runTime);
        runRepo.save(userRun);
    }

    public Map findMap(MapRepository mapRepo, String mapId) {
        long myMapId = Long.parseLong(mapId);
        Map myMap = mapRepo.findOne(myMapId);
        return myMap;
    }

    public String getUrl(Map myMap, Device device) {
        String url = myMap.getUrl();
        if (device.isMobile()) {
            url = url.replace("size=400x500", "size=335x475");
        } else {
            url = url.replace("size=250x250", "size=500x500");
        }
        return url;
    }
}
