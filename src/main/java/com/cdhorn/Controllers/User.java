package com.cdhorn.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class User {

    @RequestMapping("/user")
    public String userHome() {

        return "user";
    }

    @RequestMapping("/user/addRun")
    public String addRun() {

        return "addRun";
    }

    @RequestMapping("/user/updateRun")
    public String updateRun() {

        return "editRun";
    }
}
