package com.cdhorn.Controllers;

import com.cdhorn.Interfaces.RunRepository;
import com.cdhorn.Interfaces.UserRepository;
import com.cdhorn.Models.Run;
import com.cdhorn.Models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;


@Controller
public class UserController {

    @Autowired
    UserRepository userRepo;

    @Autowired
    RunRepository runRepo;

    @RequestMapping("/user")
    public String userHome(Model model, Principal principal) {
        User user = userRepo.findByUsername(principal.getName());
        Iterable<Run> userRuns = runRepo.findAllByUser(user);
        String urlEndpoint = "user";
        model.addAttribute("userRuns", userRuns);
        model.addAttribute("user", user);
        model.addAttribute("urlEndpoint", urlEndpoint);
        return "user";
    }

}
