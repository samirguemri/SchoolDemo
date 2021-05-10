package edu.samir.schooldemo.controller.admin;

import edu.samir.schooldemo.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;

@AllArgsConstructor
@Controller
public class AdminController {

    private final UserService userService;
}
