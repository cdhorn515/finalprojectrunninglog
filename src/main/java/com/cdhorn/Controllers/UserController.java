package com.cdhorn.Controllers;

import com.cdhorn.Interfaces.RunRepository;
import com.cdhorn.Interfaces.UserRepository;
import com.cdhorn.Models.Run;
import com.cdhorn.Models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
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
                         @RequestParam("hour") int hour,
                         @RequestParam("minute") int minute,
                         @RequestParam("second") int second,
                         Principal principal) {
        String username = principal.getName();
        User user = userRepo.findByUsername(username);
        Run userRun = new Run();
        String time = hour + ":" + minute + ":" + second;
        userRun.setDate(date);
        userRun.setDistance(distance);
        userRun.setHour(hour);
        userRun.setMinute(minute);
        userRun.setSecond(second);
        Time runTime = Time.valueOf(time);
        System.out.println(runTime);
        userRun.setTime(runTime);
        userRun.setUser(user);
        runRepo.save(userRun);
        return "addRun";
    }

    @RequestMapping(value = "/user/updateRun/{id}", method = RequestMethod.GET)
    public String updateRun(@PathVariable("id") long id,
                            Model model) {
        Run run = runRepo.findOne(id);
        model.addAttribute("run", run);
        return "editRun";
    }

    @RequestMapping(value = "/user/updateRun/{id}", method = RequestMethod.POST)
    public String updateRun(@PathVariable("id") long id,
                            @RequestParam("date") String date,
                            @RequestParam("distance") float distance,
                            @RequestParam("time") String time,
                            Model model) {
        Run userRun = runRepo.findOne(id);

        userRun.setDistance(distance);
        Date runDate = Date.valueOf(date);
        Time runTime = Time.valueOf(time);
//        System.out.println(runTime);
        userRun.setDate(runDate);
        userRun.setTime(runTime);
        runRepo.save(userRun);
        model.addAttribute(userRun);
        return "redirect:/user";
    }

    @RequestMapping(value = "/user/deleteRun", method = RequestMethod.POST)
    public String deleteRun(@RequestParam("id") long id) {
        runRepo.delete(id);
        return "redirect:/user";
    }
}
