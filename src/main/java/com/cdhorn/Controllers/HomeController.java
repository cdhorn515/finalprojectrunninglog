package com.cdhorn.Controllers;

import com.cdhorn.Classes.ApiKey;
import com.cdhorn.Interfaces.MapRepository;
import com.cdhorn.Interfaces.UserRepository;
import com.cdhorn.Models.Map;
import com.cdhorn.Models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;



@Controller
public class HomeController {

    @Autowired
    MapRepository mapRepo;
//    @Autowired
//    RunRepository runRepo;
    @Autowired
    UserRepository userRepo;

    @RequestMapping("/")
    public String index(Model model, Principal principal) {
        ApiKey apiKey = new ApiKey();
        String parksUrl = apiKey.getPARKS_URL();
        model.addAttribute("parksUrl", parksUrl);
        try {
            User user = userRepo.findByUsername(principal.getName());
            model.addAttribute("user", user);
        } catch (Exception ex) {}
        try {
            Iterable<Map> sharedMaps = mapRepo.findAllBySharedIsTrue();
            model.addAttribute("sharedMaps", sharedMaps);
        } catch (Exception e) {}
        return "index";
    }

    @RequestMapping("/getMap/{mapId}")
    public String displaySharedMap(@PathVariable("mapId") String mapId,
                                   Model model, Principal principal) {
        try {
            User user = userRepo.findByUsername(principal.getName());
            model.addAttribute("user", user);
        } catch (Exception ex) {}
        long myMapId = Long.parseLong(mapId);
        Map myMap = mapRepo.findOne(myMapId);
        String url = myMap.getUrl();
        url = url.replace("size=250x250", "size=500x500");
        url = url.replace("zoom=12", "zoom=13");
        model.addAttribute("url", url);
        return "map";
    }
}



