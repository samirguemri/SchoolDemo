package edu.samir.schooldemo.controller.teacher;

import edu.samir.schooldemo.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;

@AllArgsConstructor
@Controller
public class TeacherController {

    private final UserService userService;

}
