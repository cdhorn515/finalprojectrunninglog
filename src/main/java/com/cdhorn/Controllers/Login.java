package com.cdhorn.Controllers;

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
public class Login {

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
        model.addAttribute("user", new User());
        return "signup";
    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public String signupForm(@RequestParam("username") String username,
                         @RequestParam("password") String password,
                         @RequestParam("age") int age,
                         @RequestParam("gender") char gender,
                         @RequestParam("city") String city,
                         @RequestParam("state") String state
                         ) {
        //create new instance of user model
        User user = new User();
        //set username on user instance
        user.setUsername(username);
        user.setGender(gender);
        user.setState(state);
        user.setAge(age);
        user.setCity(city);
        //bcrypt encode pw
        String encryptedPassword = bCryptPasswordEncoder.encode(password);
        //set encrypted pw on user instance
        user.setPassword(encryptedPassword);
        //set user role on user instance
        Role userRole = roleRepo.findByName("ROLE_USER");
        user.setRole(userRole);
        user.setActive(true);
        System.out.println(user);
        userRepo.save(user);
        return "redirect:/login";
    }
}
