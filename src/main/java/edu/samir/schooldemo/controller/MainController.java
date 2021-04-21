package edu.samir.schooldemo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("web")
public class MainController {

    @GetMapping
    public String home(){
        return "home.html";
    }

    @GetMapping(path = "student")
    public String student(){
        return "student.html";
    }

    @GetMapping(path = "admin")
    public String admin(){
        return "admin.html";
    }
}
