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

import java.security.Principal;

@Controller
public class AdminController {

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private RunRepository runRepo;

    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String admin(Model model, Principal principal) {
        try {
            String username = principal.getName();
            model.addAttribute("username", username);
        } catch (Exception ex) {
            return "redirect:/login/admin";
        }
        Iterable<User> allUsers = userRepo.findByUsernameNot("admin");
        model.addAttribute("users", allUsers);
        return "admin";
    }

    @RequestMapping("/admin/user/{userId}")
    public String adminUserView(Model model,
                                @PathVariable("userId") String userId) {
        long longUserId = Long.parseLong(userId);
        User user = userRepo.findOne(longUserId);
        Iterable<Run> userRuns = runRepo.findAllByUser(user);
        model.addAttribute("userRuns", userRuns);
        model.addAttribute("user", user);
        return "user";
    }

//    @RequestMapping("/login/admin")
//    public String adminLogin(Model model, HttpServletRequest request) {
//        model.addAttribute("user", new User());
//        try {
//            Object message = request.getSession().getAttribute("error");
//            model.addAttribute("error", message);
//        } catch (Exception ex) {}
//
//        return "adminLogin";
//    }
}
