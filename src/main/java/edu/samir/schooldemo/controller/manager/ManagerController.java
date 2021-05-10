package edu.samir.schooldemo.controller.manager;

import edu.samir.schooldemo.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;

@AllArgsConstructor
@Controller
public class ManagerController {

    private final UserService userService;
}
