package com.cdhorn.Controllers;

import com.cdhorn.Classes.ApiKey;
import com.cdhorn.Interfaces.MapRepository;
import com.cdhorn.Interfaces.UserRepository;
import com.cdhorn.Models.Map;
import com.cdhorn.Models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;



@Controller
public class HomeController {

    @Autowired
    MapRepository mapRepo;
    @Autowired
    UserRepository userRepo;

    @RequestMapping("/")
    public String index(Model model, Principal principal, Device device) {
        ApiKey apiKey = new ApiKey();
        String parksUrl = apiKey.getPARKS_URL();
        if (device.isMobile()) {
            parksUrl = parksUrl.replace("size=400x500", "size=350x450");
        }
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
                                   Model model, Principal principal, Device device) {
        try {
            User user = userRepo.findByUsername(principal.getName());
            model.addAttribute("user", user);
        } catch (Exception ex) {}
        long myMapId = Long.parseLong(mapId);
        Map myMap = mapRepo.findOne(myMapId);
        String url = myMap.getUrl();
        if (device.isMobile()) {
            url = url.replace("size=400x500", "size=335x475");
        } else {

            url = url.replace("size=250x250", "size=500x500");
        }
        url = url.replace("zoom=12", "zoom=13");
        model.addAttribute("url", url);
        return "map";
    }
}



