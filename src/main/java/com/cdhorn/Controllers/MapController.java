package com.cdhorn.Controllers;

import com.cdhorn.Classes.*;
import com.cdhorn.Interfaces.*;
import com.cdhorn.Models.Map;
import com.cdhorn.Models.Run;
import com.cdhorn.Models.User;
import feign.Feign;
import feign.gson.GsonDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
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
            model.addAttribute("user", user);
            Iterable<Map> myMaps = mapRepo.findAllByUser(user);
            model.addAttribute("myMaps", myMaps);
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

        HelperFx helperFx = new HelperFx();
        String position = helperFx.getPosition(address);

        Map newMap = new Map();
        newMap.setStartPosition(position);
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
        return "redirect:/map/{runId}/routeEnd/" + mapId;
    }

    @RequestMapping("/map/{runId}/routeEnd/{mapId}")
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
                           @RequestParam("endaddress") String address,
                           Model model) {

        HelperFx helperFx = new HelperFx();
        String position = helperFx.getPosition(address);

        Map updateMap = helperFx.updateMap(mapRepo, mapId);

        updateMap.setEndPosition(position);
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
                              @RequestParam("leg") String address,
                              @PathVariable("mapId") String mapId,
                              Model model) {

        HelperFx helperFx = new HelperFx();
        String position = helperFx.getPosition(address);

        Map updateMap = helperFx.updateMap(mapRepo, mapId);

        String currentLegs = updateMap.getLegs();
        if (currentLegs == null) {
            updateMap.setLegs(position);
        } else {
            String updatedLegsString = currentLegs + "|" + position;
            updateMap.setLegs(updatedLegsString);
        }
        mapRepo.save(updateMap);
        model.addAttribute("mapId", mapId);
        model.addAttribute("runId", runId);
        return "redirect:/map/{runId}/routeLeg/{mapId}";
    }

    @RequestMapping(value = "/map/{runId}/createMap/{mapId}")
    public String testMap(@PathVariable("runId") String runId,
                          @PathVariable("mapId") String mapId) {

        HelperFx helperFx = new HelperFx();
        Map myMap = helperFx.getMap(mapRepo, runRepo, runId, mapId);

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
        HelperFx helperFx = new HelperFx();
        helperFx.getMap(mapRepo, runRepo, runId, mapId);
        return "redirect:/user";
    }

    @RequestMapping("/displayMap/{mapId}")
    public String displayMap(@PathVariable("mapId") String mapId,
                             Model model, Principal principal, Device device) {
        String username = principal.getName();
        User user = userRepo.findByUsername(username);
        model.addAttribute("user", user);
        long longMapId = Long.parseLong(mapId);
        Map myMap = mapRepo.findOne(longMapId);
        Run myRun = runRepo.findFirstByMap(myMap);
        String url = myMap.getUrl();
        if (device.isMobile()) {
            url = url.replace("size=400x500", "size=350x450");
        } else {
            url = url.replace("size=250x250", "size=500x500");
        }
        if (myRun.getDistance() > 7) {
           url = url.replace("zoom=12", "zoom=10");
        } else {
            if (myRun.getDistance() < 4) {
                url = url.replace("zoom=12", "zoom=14");
            }
        }
        String urlEndpoint = "user";
        model.addAttribute("urlEndpoint", urlEndpoint);
        model.addAttribute("url", url);
        model.addAttribute("myMap", myMap);
        return "map";
    }

}
