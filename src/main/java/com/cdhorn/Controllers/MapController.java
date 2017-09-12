package com.cdhorn.Controllers;

import com.cdhorn.Classes.*;
import com.cdhorn.Interfaces.*;
import com.cdhorn.Models.Map;
import com.cdhorn.Models.Run;
import com.cdhorn.Models.User;
import feign.Feign;
import feign.gson.GsonDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
public class MapController {

    @Autowired
    MapRepository mapRepo;

    @Autowired
    RunRepository runRepo;

    @Autowired
    UserRepository userRepo;

    @RequestMapping("/map/{runId}/routeStart")
    public String createRoute(Model model,
                              @PathVariable("runId") String runId, Principal principal) {
        try {
            User user = userRepo.findByUsername(principal.getName());
            Iterable<Map> myMaps = mapRepo.findAllByUser(user);
            model.addAttribute("myMaps", myMaps);
            model.addAttribute("user", user);
        } catch (Exception ex) {}
        model.addAttribute("runId", runId);
        return "routeStart";
    }

    @RequestMapping(value = "/map/{runId}/routeStart", method = RequestMethod.POST)
    public String createRoute(@PathVariable("runId") String runId,
                              @RequestParam("address") String address,
                              @RequestParam("route_name") String route_name,
                              @RequestParam("shared") String shared,
                              Model model) {
        String addressNoSpaces = address.replace(" ", "+");
        ApiKey apiKey = new ApiKey();
        GeocodingInterface geocodingInterface = Feign.builder()
                .decoder(new GsonDecoder())
                .target(GeocodingInterface.class, "https://maps.googleapis.com");
        GeocodingResponse response = geocodingInterface.geocodingResponse(addressNoSpaces + "+greenville+sc",
                apiKey.getGEOCODING_API());
        double lat = response.getResults().get(0).getGeometry().getLocation().getLat();
        double lng = response.getResults().get(0).getGeometry().getLocation().getLng();
        String startPosition = lat + "," + lng;
        Map newMap = new Map();
        newMap.setStartPosition(startPosition);
        newMap.setRouteName(route_name);
        if (shared.equals("Y")) {
            newMap.setShared(true);
        }
        long intRunId = Integer.valueOf(runId);
        Run myRun = runRepo.findOne(intRunId);
        User user = myRun.getUser();
        newMap.setUser(user);
        mapRepo.save(newMap);
        long mapId = newMap.getId();
        model.addAttribute("mapId", mapId);
        model.addAttribute("runId", runId);
        return "redirect:/map/{runId}/routeEnd/" + mapId + "/" + address;
    }

    @RequestMapping("/map/{runId}/routeEnd/{mapId}/{address}")
    public String routeEnd(@PathVariable("runId") String runId,
                           @PathVariable("mapId") String mapId,
                           Model model, Principal principal) {
        User user = userRepo.findByUsername(principal.getName());
        model.addAttribute("user", user);
        model.addAttribute("mapId", mapId);
        model.addAttribute("runId", runId);

        return "routeEnd";
    }

    @RequestMapping(value = "/map/{runId}/routeEnd/{mapId}", method = RequestMethod.POST)
    public String routeEnd(@PathVariable("runId") String runId,
                           @PathVariable("mapId") String mapId,
                           @RequestParam("endaddress") String endaddress,
                           Model model) {

        String addressNoSpaces = endaddress.replace(" ", "+");

        ApiKey apiKey = new ApiKey();
        GeocodingInterface geocodingInterface = Feign.builder()
                .decoder(new GsonDecoder())
                .target(GeocodingInterface.class, "https://maps.googleapis.com");
        GeocodingResponse response = geocodingInterface.geocodingResponse(addressNoSpaces + "+greenville+sc", apiKey.getGEOCODING_API());
        double lat = response.getResults().get(0).getGeometry().getLocation().getLat();
        double lng = response.getResults().get(0).getGeometry().getLocation().getLng();
        String endPosition = lat + "," + lng;

        long intMapId = Integer.valueOf(mapId);
        Map updateMap = mapRepo.findOne(intMapId);

        updateMap.setEndPosition(endPosition);
        mapRepo.save(updateMap);
        model.addAttribute("mapId", mapId);
        model.addAttribute("runId", runId);

        return "redirect:/map/{runId}/routeLeg/{mapId}";
    }

    @RequestMapping("/map/{runId}/routeLeg/{mapId}")
    public String addRouteLeg(@PathVariable("runId") String runId,
                              @PathVariable("mapId") String mapId,
                              Model model, Principal principal) {
        User user = userRepo.findByUsername(principal.getName());
        model.addAttribute("user", user);
        model.addAttribute("mapId", mapId);
        model.addAttribute("runId", runId);
        return "routeLeg";
    }

    @RequestMapping(value = "/map/{runId}/routeLeg/{mapId}", method = RequestMethod.POST)
    public String addRouteLeg(@PathVariable("runId") String runId,
                              @RequestParam("leg") String leg,
                              @PathVariable("mapId") String mapId,
                              Model model) {

        String addressNoSpaces = leg.replace(" ", "+");
        ApiKey apiKey = new ApiKey();
        GeocodingInterface geocodingInterface = Feign.builder()
                .decoder(new GsonDecoder())
                .target(GeocodingInterface.class, "https://maps.googleapis.com");
        GeocodingResponse response = geocodingInterface.geocodingResponse(addressNoSpaces + "+greenville+sc", apiKey.getGEOCODING_API());
        double lat = response.getResults().get(0).getGeometry().getLocation().getLat();
        double lng = response.getResults().get(0).getGeometry().getLocation().getLng();
        String legs = lat + "," + lng;
        long intMapId = Integer.valueOf(mapId);
        Map updateMap = mapRepo.findOne(intMapId);
        String currentLegs = updateMap.getLegs();
        if (currentLegs == null) {
            updateMap.setLegs(legs);
        } else {
            String updatedLegsString = currentLegs + "|" + legs;
            updateMap.setLegs(updatedLegsString);
        }
        mapRepo.save(updateMap);
        model.addAttribute("mapId", mapId);
        model.addAttribute("runId", runId);
        return "redirect:/map/{runId}/routeLeg/" + intMapId;
    }

    @RequestMapping(value = "/map/{runId}/createMap/{mapId}")
    public String testMap(@PathVariable("runId") String runId,
                          @PathVariable("mapId") String mapId) {
        long myMapId = Long.parseLong(mapId);
        Map myMap = mapRepo.findOne(myMapId);
        long myRunId = Long.parseLong(runId);
        Run myRun = runRepo.findOne(myRunId);
        myRun.setMap(myMap);
        runRepo.save(myRun);
        String startPosition = myMap.getStartPosition();
        String endPosition = myMap.getEndPosition();
        String waypoints = myMap.getLegs();
        ApiKey apiKey = new ApiKey();
        DirectionsInterface directionsInterface = Feign.builder()
                .decoder(new GsonDecoder())
                .target(DirectionsInterface.class, "https://maps.googleapis.com");
        DirectionResponse response = directionsInterface.directionResponse(startPosition, endPosition, waypoints, apiKey
                .getDIRECTIONS_API());
        String polyline = response.getRoutes().get(0).getOverview_polyline().getPoints();

        ApiStaticMap apiStaticMap = new ApiStaticMap();
        String url = apiStaticMap.getStaticMapUrl();
        String startMarker = apiStaticMap.getStartMarkerParameters();
        String finishMarker = apiStaticMap.getFinishMarkerParameters();
        String pathParams = apiStaticMap.getPathParameters();
        String staticMapApiKeyParams = apiStaticMap.getStaticMapApiKey();

        url += startMarker + startPosition + finishMarker + endPosition + staticMapApiKeyParams + apiKey.getSTATIC_MAP_API() + pathParams + polyline;

        myMap.setUrl(url);
        mapRepo.save(myMap);
        return "redirect:/user";
    }

    @RequestMapping(value = "/map/{runId}/routeSelect", method = RequestMethod.POST)
    public String selectPreviousRoute(@PathVariable("runId") String runId,
                                      @RequestParam("map_id") String mapId) {
        long myMapId = Long.parseLong(mapId);
        Map myMap = mapRepo.findOne(myMapId);
        long myRunId = Long.parseLong(runId);
        Run myRun = runRepo.findOne(myRunId);
        myRun.setMap(myMap);
        runRepo.save(myRun);
        return "redirect:/user";
    }

    @RequestMapping("/displayMap/{mapId}")
    public String displayMap(@PathVariable("mapId") String mapId,
                             Model model, Principal principal) {
        String username = principal.getName();
        User user = userRepo.findByUsername(username);
        model.addAttribute("user", user);
        long longMapId = Long.parseLong(mapId);
        Map myMap = mapRepo.findOne(longMapId);
        Run myRun = runRepo.findFirstByMap(myMap);
        String url = myMap.getUrl();
        url = url.replace("250x250", "500x500");
        if (myRun.getDistance() > 7) {
           url = url.replace("zoom=12", "zoom=10");
        } else {
            if (myRun.getDistance() < 4) {
                url = url.replace("zoom=12", "zoom=14");
            }
        }
        model.addAttribute("url", url);
        return "map";
    }

}
