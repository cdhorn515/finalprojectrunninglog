package com.cdhorn.Controllers;

import com.cdhorn.Interfaces.RunRepository;
import com.cdhorn.Interfaces.UserRepository;
import com.cdhorn.Models.Run;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import com.cdhorn.Models.User;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.sql.Date;
import java.sql.Time;


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
        model.addAttribute("userRuns", userRuns);
        model.addAttribute("user", user);
        return "user";
    }

    @RequestMapping("/user/addRun")
    public String addRun(Model model) {
        model.addAttribute("newRun", new Run());
        return "addRun";
    }

    @RequestMapping(value = "/user/addRun", method = RequestMethod.POST)
    public String addRun(@RequestParam("date") Date date,
                         @RequestParam("distance") float distance,
                         @RequestParam("time") Time time,
                         Principal principal) {
        String username = principal.getName();
        User user = userRepo.findByUsername(username);
        Run userRun = new Run();
        userRun.setDate(date);
        userRun.setDistance(distance);
        userRun.setTime(time);
        userRun.setUser(user);
        runRepo.save(userRun);
        return "addRun";
    }

    @RequestMapping("/user/updateRun")
    public String updateRun() {

        return "editRun";
    }
}
