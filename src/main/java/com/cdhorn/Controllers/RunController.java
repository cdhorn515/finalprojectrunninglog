package com.cdhorn.Controllers;

import com.cdhorn.Classes.HelperFx;
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
import java.text.SimpleDateFormat;

@Controller
public class RunController {

    @Autowired
    UserRepository userRepo;

    @Autowired
    RunRepository runRepo;

    @RequestMapping("/user/addRun")
    public String addRun(Model model, Principal principal) {
        User user = userRepo.findByUsername(principal.getName());
        model.addAttribute("user", user);
        model.addAttribute("newRun", new Run());
        return "addRun";
    }

    @RequestMapping(value = "/user/addRun", method = RequestMethod.POST)
    public String addRun(@RequestParam("date") Date date,
                         @RequestParam("distance") float distance,
                         @RequestParam("hour") String hour,
                         @RequestParam("minute") String minute,
                         @RequestParam("second") String second,
                         Principal principal, Model model) {
        User user = userRepo.findByUsername(principal.getName());
        Run userRun = new Run();
        if (hour == "") {
            hour = "00";
        }
        if (minute == "") {
            minute = "00";
        }
        if (second == "") {
            second = "00";
        }
        String time = hour + ":" + minute + ":" + second;
        userRun.setUser(user);
        userRun.setDate(date);
        HelperFx helperFx = new HelperFx();
        helperFx.setRunData(time, distance, userRun, runRepo);
        long runId = userRun.getId();
        model.addAttribute("runId", runId);
        return "redirect:/map/"+ runId + "/routeStart";
    }

    @RequestMapping(value = "/user/updateRun/{id}", method = RequestMethod.GET)
    public String updateRun(@PathVariable("id") long id,
                            Model model, Principal principal) {
        Run run = runRepo.findOne(id);
        User user = userRepo.findByUsername(principal.getName());
        model.addAttribute("user", user);
        model.addAttribute("run", run);
        return "editRun";
    }

    @RequestMapping(value = "/user/updateRun/{id}", method = RequestMethod.POST)
    public String updateRun(@PathVariable("id") long id,
                            @RequestParam("date") String date,
                            @RequestParam("distance") float distance,
                            @RequestParam("time") String time) throws Exception {
        Run userRun = runRepo.findOne(id);
        java.util.Date runDate = new SimpleDateFormat("yyyy-MM-dd").parse(date);
        userRun.setDate(runDate);
        HelperFx helperFx = new HelperFx();
        helperFx.setRunData(time, distance, userRun, runRepo);
        return "redirect:/user";
    }

    @RequestMapping(value = "/user/deleteRun", method = RequestMethod.POST)
    public String deleteRun(@RequestParam("id") long id) {
        runRepo.delete(id);
        return "redirect:/user";
    }
}
