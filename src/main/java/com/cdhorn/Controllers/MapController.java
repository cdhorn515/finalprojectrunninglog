package com.cdhorn.Controllers;

import com.cdhorn.GoogleMaps.ApiGeocoding;
import com.cdhorn.GoogleMaps.ApiKey;
import com.cdhorn.Interfaces.GoogleMapsInterface;
import feign.Feign;
import feign.gson.GsonDecoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Controller
public class MapController {

    @RequestMapping("/routeStart")
    public String createRoute(Model model) throws IOException {

        ApiKey apiKey = new ApiKey();
        ApiGeocoding apiGeocoding = new ApiGeocoding();
        GoogleMapsInterface gmaps = Feign.builder()
                .decoder(new GsonDecoder())
                .target(GoogleMapsInterface.class, "https://maps.googleapis.com");
        GeocodingResponse response = gmaps.geocodingResponse("starbucks+east+north+street+greenville+sc",
                "AIzaSyBhMVxBkMr2Q4Gl7EVvtcdrNu7WpS2srkw");
        System.out.println(response.results.get(0).geometry.location.lat);


//        HttpClient client = new DefaultHttpClient();
//        HttpGet geocodeRequest = new HttpGet("https://maps.googleapis.com/maps/api/geocode/json?address=starbucks+east+north+street+greenville+sc&key=" + apiKey.getGEOCODING_API());
//        HttpResponse geocodeResponse = client.execute(geocodeRequest);
//        System.out.println(geocodeResponse);
//        BufferedReader rd = new BufferedReader(new InputStreamReader(geocodeResponse.getEntity().getContent()));
//        String geoCodeLine = rd.readLine();
//        String line = "";
//        while (geoCodeLine != null) {
//            line += geoCodeLine;
//            geoCodeLine = rd.readLine();
//        }
//        ObjectMapper mapper = new ObjectMapper();
//        ApiGeocodingFeed geocodingFeed = mapper.readValue(line, ApiGeocodingFeed.class);
//        ApiGeocodingGeometryObject geometryObject = mapper.convertValue(geocodingFeed.getResult().get(0), new TypeReference<ApiGeocodingGeometryObject>() {});
//        LinkedHashMap geocodingLocationObject = geometryObject.getApiGeocodingLocationObject();
//
//        ApiGeocodingLocationObject locationObject = mapper.convertValue(geometryObject.getApiGeocodingLocationObject(), new TypeReference<ApiGeocodingLocationObject>() {});
//
//        System.out.println(geocodingLocationObject);
//        System.out.println(geocodingFeed.getResult().get(0).getApiGeocodingGeometryObject());
//        System.out.println(line);

        return "createRoute";
    }

    @RequestMapping(value = "/routeStart", method = RequestMethod.POST)
    public String createRoute(@RequestParam("address") String address) {

        return "addLeg";
    }

    @RequestMapping("/map")
    public String testMap(Model model) throws IOException {

        ApiKey apiKey = new ApiKey();
//        HttpClient client = new DefaultHttpClient();
//        HttpGet request = new HttpGet("https://maps.googleapis.com/maps/api/directions/json?origin=furman+university+greenville+SC&destination=swamp+rabbit+grocery+greenville+sc&key=" + apiKey.getDIRECTIONS_API() + "&mode=walking");
//        HttpResponse response = client.execute(request);
//
//        System.out.println(response);
//        System.out.println("");
//        BufferedReader rd = new BufferedReader (new InputStreamReader(response.getEntity().getContent()));
//
//        String mapLine = rd.readLine();
//        String line = "";
//        while (mapLine != null) {
//            line += mapLine;
//            mapLine = rd.readLine();
//        }
//        System.out.println(line);
//        ObjectMapper mapper = new ObjectMapper();
//        ApiDirectionsFeed feed = mapper.readValue(line, ApiDirectionsFeed.class);
//        System.out.println("");
//        List<Routes> x = feed.getRoutes();
//        Routes myRoute = mapper.convertValue(feed.getRoutes().get(0), new TypeReference<Routes>() {});
//        LinkedHashMap polylineObject = myRoute.getOverviewPolylineObject();
//        Object myPoints = polylineObject.get("points");
//        System.out.println(myPoints);
//        String polylineString = myPoints.toString();
//
//        String polylineStringReformatted = polylineString.replace("|", "%7C");
//        System.out.println(polylineStringReformatted);
//
//
//        //concat url string
//        ApiStaticMap apiStaticMap = new ApiStaticMap();
//        String url = apiStaticMap.getStaticMapUrl();
//        String startMarker = apiStaticMap.getStartMarkerParameters();
//        String finishMarker = apiStaticMap.getFinishMarkerParameters();
//        String pathParams = apiStaticMap.getPathParameters();
//        String staticMapApiKeyParams = apiStaticMap.getStaticMapApiKey();
//         url += startMarker + "34.9195746,-82.4217151" + finishMarker + "34.8701836,-82.4217151" + staticMapApiKeyParams + apiKey.getSTATIC_MAP_API() + pathParams + polylineStringReformatted;
//
//        System.out.println(url);
//        model.addAttribute("url", url);
        return "map";

    }

}
