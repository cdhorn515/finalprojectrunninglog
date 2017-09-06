package com.cdhorn.Controllers;

import com.cdhorn.GoogleMaps.ApiDirectionsFeed;
import com.cdhorn.GoogleMaps.ApiKey;
import com.cdhorn.GoogleMaps.ApiStaticMap;
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
        List<Routes> x = feed.getRoutes();
        Routes myRoute = mapper.convertValue(feed.getRoutes().get(0), new TypeReference<Routes>() {});
        LinkedHashMap polylineObject = myRoute.getOverviewPolylineObject();
        Object myPoints = polylineObject.get("points");
        System.out.println(myPoints);
        String polylineString = myPoints.toString();
//        System.out.println(polylineString);

        String polylineStringReformatted = polylineString.replace("|", "%7C");
        System.out.println(polylineStringReformatted);

        Object polylineObjectReformatted = polylineStringReformatted;
        System.out.println("===================================================");
        System.out.println("");
//        System.out.println(polylineObjectReformatted);

        ApiStaticMap apiStaticMap = new ApiStaticMap();
        String url = apiStaticMap.getStaticMapUrl();
        String startMarker = apiStaticMap.getStartMarkerParameters();
        String finishMarker = apiStaticMap.getFinishMarkerParameters();
        String pathParams = apiStaticMap.getPathParameters();
        String staticMapApiKeyParams = apiStaticMap.getStaticMapApiKey();
         url += startMarker + "34.9195746,-82.4217151" + finishMarker + "34.8701836,-82.4217151" + staticMapApiKeyParams + apiKey.getSTATIC_MAP_API() + pathParams + polylineStringReformatted;

        System.out.println(url);
        model.addAttribute("url", url);
        return "map";

    }

}
