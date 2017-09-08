package com.cdhorn.Controllers;

import com.cdhorn.Classes.DirectionResponse;
import com.cdhorn.Classes.GeocodingResponse;
import com.cdhorn.GoogleMaps.ApiKey;
import com.cdhorn.GoogleMaps.ApiStaticMap;
import com.cdhorn.Interfaces.DirectionsInterface;
import com.cdhorn.Interfaces.GeocodingInterface;
import com.cdhorn.Interfaces.MapRepository;
import com.cdhorn.Interfaces.RunRepository;
import com.cdhorn.Models.Map;
import com.cdhorn.Models.Run;
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
        mapRepo.save(newMap);
        long mapId = newMap.getId();
        System.out.println(response.getResults().get(0).getGeometry().getLocation().getLat());
        System.out.println(response.getResults().get(0).getGeometry().getLocation().getLng());
        model.addAttribute("mapId", mapId);
        model.addAttribute("runId", runId);
//        model.addAttribute("address", address);
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
        System.out.println("--------------------------------------------------------");
        System.out.println(endPosition);
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
            String updatedLegsString = currentLegs + "%7C" + legs;
            updateMap.setLegs(updatedLegsString);
        }

        mapRepo.save(updateMap);
        model.addAttribute("mapId", mapId);
        model.addAttribute("runId", runId);
        return "redirect:/map/{runId}/routeLeg/" + intMapId;
    }

    @RequestMapping(value = "/map/{runId}/createMap/{mapId}")
    public String testMap(@PathVariable("runId") String runId,
                          @PathVariable("mapId") String mapId,
                          Model model) {
        long intMapId = Integer.valueOf(mapId);
        Map myMap = mapRepo.findOne(intMapId);
        long intRunId = Integer.valueOf(runId);
        Run myRun = runRepo.findOne(intRunId);
        myRun.setMap(myMap);
        runRepo.save(myRun);
        String startPosition = myMap.getStartPosition();
        String endPosition = myMap.getEndPosition();
        ApiKey apiKey = new ApiKey();
        DirectionsInterface directionsInterface = Feign.builder()
                .decoder(new GsonDecoder())
                .target(DirectionsInterface.class, "https://maps.googleapis.com");
        DirectionResponse response = directionsInterface.directionResponse(startPosition, endPosition, apiKey.getDIRECTIONS_API());
        String polyline = response.getRoutes().get(0).getOverview_polyline().getPoints();
        System.out.println(polyline);
        System.out.println("");
        String reformattedPolyline = polyline.replace("|", "%7C");
        System.out.println(reformattedPolyline);
        System.out.println("");

        ApiStaticMap apiStaticMap = new ApiStaticMap();
        String url = apiStaticMap.getStaticMapUrl();
        String startMarker = apiStaticMap.getStartMarkerParameters();
        String finishMarker = apiStaticMap.getFinishMarkerParameters();
        String pathParams = apiStaticMap.getPathParameters();
        String staticMapApiKeyParams = apiStaticMap.getStaticMapApiKey();

        url += startMarker + startPosition + finishMarker + endPosition + staticMapApiKeyParams + apiKey.getSTATIC_MAP_API() + pathParams + reformattedPolyline;
//
        myMap.setUrl(url);
        mapRepo.save(myMap);
        System.out.println(url);
        model.addAttribute("url", url);
        return "map";

    }

}
