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
        String cleveland = apiKey.getCLEVELAND_URL();
        String falls = apiKey.getFALLS_URL();
        String paris = apiKey.getPARIS_URL();
        String conestee = apiKey.getCONESTEE_URL();
        if (device.isMobile()) {
            parksUrl = parksUrl.replace("size=400x500", "size=350x450");
            cleveland = cleveland.replace("size=500x500", "size=350x350");
            falls = falls.replace("size=500x500", "size=350x350");
            paris = paris.replace("size=500x500", "size=350x350");
            conestee = conestee.replace("size=500x500", "size=350x350");
        }
        model.addAttribute("parksUrl", parksUrl);
        model.addAttribute("cleveland", cleveland);
        model.addAttribute("falls", falls);
        model.addAttribute("paris", paris);
        model.addAttribute("conestee", conestee);
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



