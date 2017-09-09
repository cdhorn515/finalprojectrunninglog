package com.cdhorn.Controllers;

import com.cdhorn.Classes.ApiKey;
import com.cdhorn.Classes.ApiStaticMap;
import com.cdhorn.Classes.DirectionResponse;
import com.cdhorn.Classes.GeocodingResponse;
import com.cdhorn.Interfaces.DirectionsInterface;
import com.cdhorn.Interfaces.GeocodingInterface;
import com.cdhorn.Interfaces.MapRepository;
import com.cdhorn.Interfaces.RunRepository;
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

@Controller
public class MapController {

    @Autowired
    MapRepository mapRepo;

    @Autowired
    RunRepository runRepo;

    @RequestMapping("/map/{runId}/routeStart")
    public String createRoute(Model model,
                              @PathVariable("runId") String runId) {

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
        if (shared == "Y") {
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
                           Model model) {
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
                              Model model) {
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
        long intMapId = Integer.valueOf(mapId);
        Map myMap = mapRepo.findOne(intMapId);
        long intRunId = Integer.valueOf(runId);
        Run myRun = runRepo.findOne(intRunId);
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

    @RequestMapping("/map/{runId}")
    public String displayMap(@PathVariable("runId") String runId,
                             Model model) {
        long intRunId = Integer.valueOf(runId);
        Run myRun = runRepo.findOne(intRunId);

        Map myMap = myRun.getMap();
        String url = myMap.getUrl();
        if (myRun.getDistance() > 5) {
           url = url.replace("zoom=14", "zoom=10");
        }
        model.addAttribute("url", url);
        return "map";
    }

}
