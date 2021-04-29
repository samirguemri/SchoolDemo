package edu.samir.schooldemo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MainController {

    @GetMapping(path = "/web/student")
    public String student(){
        return "student.html";
    }

    @GetMapping(path = "/web/admin")
    public String admin(){
        return "admin.html";
    }
}
