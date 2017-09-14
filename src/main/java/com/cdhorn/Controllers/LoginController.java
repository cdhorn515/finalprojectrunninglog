package com.cdhorn.Controllers;

import com.cdhorn.Enums.StateAbbreviations;
import com.cdhorn.Interfaces.RoleRepository;
import com.cdhorn.Interfaces.UserRepository;
import com.cdhorn.Models.Role;
import com.cdhorn.Models.User;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    RoleRepository roleRepo;

    @Autowired
    UserRepository userRepo;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model, HttpServletRequest request) {
        model.addAttribute("user", new User());
        try {
            Object message = request.getSession().getAttribute("error");
            model.addAttribute("error", message);
            request.getSession().removeAttribute("error");
        } catch (Exception ex) {}
        return "login";
    }

    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public String signupForm(Model model) {
        StateAbbreviations[] states = StateAbbreviations.values();
        model.addAttribute("user", new User());
        model.addAttribute("states", states);
        return "signup";
    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public String signupForm(@RequestParam("username") String username,
                             @RequestParam("password") String password,
                             @RequestParam("age") int age,
                             @RequestParam("gender") char gender,
                             @RequestParam("city") String city,
                             @RequestParam("state") String state,
                             @RequestParam("first") String first,
                             @RequestParam("last") String last) {
        User user = new User();
        user.setUsername(username);
        if (gender == 'F' || gender == 'M') {
            user.setGender(gender);
        } else {
            user.setGender('F');
        }
        user.setAge(age);
        user.setCity(city);
        StateAbbreviations selectedState = StateAbbreviations.valueOf(state);
        user.setState(selectedState);
        user.setFirst(first);
        user.setLast(last);
        String encryptedPassword = bCryptPasswordEncoder.encode(password);
        user.setPassword(encryptedPassword);
        Role userRole = roleRepo.findByName("ROLE_USER");
        user.setRole(userRole);
        user.setActive(true);
        System.out.println(user);
        userRepo.save(user);
        return "redirect:/login";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public String logOut() {
        return "redirect:/";
    }
}
