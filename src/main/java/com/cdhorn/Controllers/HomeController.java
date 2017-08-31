package com.cdhorn.Controllers;

import com.cdhorn.Interfaces.RunRepository;
import com.cdhorn.Models.Run;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.Principal;



@Controller
public class HomeController {

    @Autowired
    RunRepository runRepo;

    @RequestMapping("/")
    public String index(Model model, Principal principal) {
        try {
            String username = principal.getName();
            model.addAttribute("username", username);
        } catch (Exception ex) {}
        Iterable<Run> userRuns = runRepo.findAll();
        model.addAttribute("runs", userRuns);
        return "index";
    }

    @RequestMapping("/map")
    public String testMap(Model model) throws IOException{


        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet("https://maps.googleapis.com/maps/api/directions/json?origin=swamp+rabbit+grocery+greenville+SC&destination=peace+center+greenville+sc&key=AIzaSyA-TjAN8NDzb32eBGEHsn4m12vX0689QRA&mode=walking");
        HttpResponse response = client.execute(request);
        System.out.println(response);
        BufferedReader rd = new BufferedReader (new InputStreamReader(response.getEntity().getContent()));
        String line = rd.readLine();
        System.out.println(line);
        ObjectMapper mapper = new ObjectMapper();

        /*
        ObjectMapper mapper = new ObjectMapper();
        StarWars starWars = mapper.readValue(line, StarWars.class);
        System.out.println(starWars);
        String name = starWars.getName();
        System.out.println(name);
        ArrayList<String> vehicles = starWars.getVehicles();
        System.out.println(vehicles);
        System.out.println(vehicles.get(0));

        System.out.println(mapper.writeValueAsString(starWars));


         */

        return "map";


    }
}
        /*
        HttpClient newClient = new DefaultHttpClient();
        HttpGet newRequest = new HttpGet("http://maps.googleapis.com/maps/api/staticmap?zoom=10&size=300x300&markers=size:mid%7Ccolor:green%7Clabel:S%7CFurman+University+Greenville,SC&markers=size:mid%7Ccolor:red%7Clabel:F%7CBob+Jones+University+Greenville,SC&key=AIzaSyCecE50bCCZBpd3m3sLbDP-oGEXC7MUdz0&path=color:blue%7Cgeodesic::true%7CFurman+University+Greenville,SC%7CBob+Jones+University+Greenville,SC");
        org.apache.http.HttpResponse newResponse = newClient.execute(newRequest);

        // CONVERT RESPONSE TO BASE64 ENCODED STRING
        long sizeOf = newResponse.getEntity().getContentLength();
        InputStream finput = newResponse.getEntity().getContent();
        byte[] imageBytes = new byte[(int)sizeOf];
        finput.read(imageBytes, 0, (int)sizeOf);
        finput.close();
        System.out.println(imageBytes);
        String imageStr = Base64.getEncoder().encodeToString(imageBytes);

        // add base64 encoded string to model
        model.addAttribute("mapBase64", imageStr);
        */
        /*
        BufferedReader reader = new BufferedReader(new InputStreamReader(newResponse.getEntity().getContent()));
        String mapLine = reader.readLine();
        String line = "";
        while (mapLine != null) {
            line += mapLine;
            mapLine = reader.readLine();
            //System.out.println(line);
        }

        String encodedImage = Base64.getEncoder().encodeToString(line.getBytes("utf-8"));
        //System.out.println(encodedImage);
        */


/*
        try {
            BufferedImage img = ImageIO.read(new URL("https://maps.googleapis.com/maps/api/staticmap?zoom=10&size=300x300&markers=size:mid%7Ccolor:green%7Clabel:S%7CFurman+University+Greenville,SC&markers=size:mid%7Ccolor:red%7Clabel:F%7CBob+Jones+University+Greenville,SC&key=AIzaSyCecE50bCCZBpd3m3sLbDP-oGEXC7MUdz0&path=color:blue%7Cgeodesic::true%7CFurman+University+Greenville,SC%7CBob+Jones+University+Greenville,SC"));

            File outputfile = new File("map.png");
            ImageIO.write(img, "png", outputfile);
            System.out.println("Saved!");
            model.addAttribute("png", outputfile);
            return "map";
        } catch (Exception ex) {
            System.out.println("Error!" + ex);
            return "redirect:/";
        }
        */


