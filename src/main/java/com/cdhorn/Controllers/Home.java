package com.cdhorn.Controllers;

import com.cdhorn.Interfaces.RunRepository;
import com.cdhorn.Models.Run;
import com.cdhorn.Models.User;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
public class Home {

    @Autowired
    RunRepository runRepo;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model, HttpServletRequest request) {
        model.addAttribute("user", new User());
        try {
            Object message = request.getSession().getAttribute("error");
            model.addAttribute("error", message);
        } catch (Exception ex) {}
        return "login";
    }

    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public String signup() {
        return "signup";
    }


    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public String signup(@RequestParam("username") String username,
                         @RequestParam("password") String password,
                         @RequestParam("age") String age,
                         @RequestParam("gender") char gender,
                         @RequestParam("city") String city,
                         @RequestParam("state") String state) {

        return "redirect:/login";
    }

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
}
