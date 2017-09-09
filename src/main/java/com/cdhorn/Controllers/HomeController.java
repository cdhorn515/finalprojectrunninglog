package com.cdhorn.Controllers;

import com.cdhorn.Interfaces.MapRepository;
import com.cdhorn.Interfaces.RunRepository;
import com.cdhorn.Interfaces.UserRepository;
import com.cdhorn.Models.Map;
import com.cdhorn.Models.Run;
import com.cdhorn.Models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
            String username = principal.getName();
            model.addAttribute("username", username);
            User user = userRepo.findByUsername(username);
            Iterable<Run> userRuns = runRepo.findAllByUser(user);
            model.addAttribute("runs", userRuns);
        } catch (Exception ex) {}
        Iterable<Map> sharedMaps = mapRepo.findAllBySharedIsTrue();

        model.addAttribute("sharedMaps", sharedMaps);
        return "index";
    }

}



