package com.cdhorn.Controllers;

import com.cdhorn.GoogleMaps.ApiDirectionsFeed;
import com.cdhorn.GoogleMaps.ApiKey;
import com.cdhorn.GoogleMaps.Routes;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.List;

@Controller
public class MapController {


    @RequestMapping("/map")
    public String testMap(Model model) throws IOException, ClassCastException {



        ApiKey apiKey = new ApiKey();
        System.out.println(apiKey.getDIRECTIONS_API());
        System.out.println(apiKey.getGEOCODING_API());
        System.out.println(apiKey.getSTATIC_MAP_API());

        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet("https://maps.googleapis.com/maps/api/directions/json?origin=furman+university+greenville+SC&destination=swamp+rabbit+grocery+greenville+sc&key=" + apiKey.getDIRECTIONS_API() + "&mode=walking");
        HttpResponse response = client.execute(request);


        System.out.println(response);
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        System.out.println("");
        BufferedReader rd = new BufferedReader (new InputStreamReader(response.getEntity().getContent()));

        String mapLine = rd.readLine();
        String line = "";
        while (mapLine != null) {
            line += mapLine;
            mapLine = rd.readLine();
        }
        System.out.println(line);
        ObjectMapper mapper = new ObjectMapper();
        ApiDirectionsFeed feed = mapper.readValue(line, ApiDirectionsFeed.class);
        System.out.println("----------------------------------------------------");
        System.out.println("");
        //System.out.println(feed.getRoutes().get(0).getOverviewPolylineObject().getPolyline());
        List<Routes> x = feed.getRoutes();
        Routes myRoute = mapper.convertValue(feed.getRoutes().get(0), new TypeReference<Routes>() {});
        //Routes myRoute = x.get(0);
        LinkedHashMap polylineObject = myRoute.getOverviewPolylineObject();
        Object myPoints = polylineObject.get("points");
        System.out.println(myPoints);

        // System.out.println(feed.getRoutes().get(0).get("overview_polyline").getPoints());
        // System.out.println(feed.getRoutes().get(0).get("overview_polyline").get("points"));
        // System.out.println(feed.getRoutes().get(0).containsKey("overview_polyline"));
//        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
//        for (Routes routes : feed.getRoutes()) {
//            System.out.println(routes.getOverviewPolylineObject().getPolyline());
//        }



        //below throws java.lang.ClassCastException: java.util.LinkedHashMap cannot be cast to com.cdhorn.GoogleMaps.Routes
//        for (Routes routes : feed.getOverviewPolylineObject()) {
//            System.out.println(routes.getOverviewPolylineObject().getPolyline());
//        }

//        String replacePipe = apiUrl.replace("|", "%7C");
//            System.out.println(replacePipe);
//        String mapUrl = replacePipe.replace("//", "/");
//        model.addAttribute("mapUrl", mapUrl);
        return "map";

    }

}
