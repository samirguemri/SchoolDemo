package edu.samir.schooldemo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MainController {

    @GetMapping(path = "/welcome")
    public String home(){
        return "home";
    }

    @GetMapping(path = "/student/")
    public String student(){
        return "web/student";
    }

    @GetMapping(path = "/admin")
    public String admin(){
        return "web/admin";
    }
}
