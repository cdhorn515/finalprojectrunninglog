package com.cdhorn.Controllers;

import com.cdhorn.Interfaces.MapRepository;
import com.cdhorn.Interfaces.RunRepository;
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
    @Autowired
    RunRepository runRepo;
    @Autowired
    UserRepository userRepo;

    @RequestMapping("/")
    public String index(Model model, Principal principal) {
        try {
//            String user = principal.getName();
            User user = userRepo.findByUsername(principal.getName());

            model.addAttribute("user", user);
//            User newUser = userRepo.findByUsername(user);
//            Iterable<Run> userRuns = runRepo.findAllByUser(newUser);
//            model.addAttribute("runs", userRuns);
        } catch (Exception ex) {}
        Iterable<Map> sharedMaps = mapRepo.findAllBySharedIsTrue();
        model.addAttribute("sharedMaps", sharedMaps);
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



