package edu.samir.schooldemo.controller;

import edu.samir.schooldemo.controller.dto.UserConverter;
import edu.samir.schooldemo.controller.dto.UserDto;
import edu.samir.schooldemo.persistence.entity.UserEntity;
import edu.samir.schooldemo.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;

@AllArgsConstructor
@RestController
public class TestController {

    private final UserService userService;
    private final UserConverter userConverter;

    @RequestMapping(path = "/welcome")
    public String home(Principal principal){
        if (principal == null)
            return "welcome home Anonymous";
        return "welcome home " + principal.getName();
    }

    @GetMapping("/users/{role}")
    public List<UserDto> usersByRole(@PathVariable String role) {
        List<UserEntity> users = userService.selectUserByRole(role);
        return userConverter.entitiesToDtos(users);
    }

    @GetMapping("/secure")
    public String welcome() {
        return "This is secured endpoint !";
    }

    @GetMapping(path = "/api/course")
    public String courseRead(){
        return "course read";
    }

    @PostMapping(path = "/api/course")
    public String courseWrite(){
        return "course write";
    }

    @GetMapping(path = "/api/student")
    public String student(HttpServletRequest request){
        CsrfToken csrfToken = ((CsrfToken) request.getAttribute("_csrf"));
        System.out.println("Controller CSRF Token " + csrfToken.getToken());
        return "student";
    }

    @PutMapping(path = "/api/student")
    public String studentUpdate(){
        return "student update";
    }

    @DeleteMapping(path = "/api/student")
    public String studentDelete(){
        return "student delete";
    }

    @GetMapping(path = "/api/teacher")
    public String teacher(){
        return "teacher";
    }

    @GetMapping(path = "/api/admin")
    public String admin(){
        return "admin";
    }

    @GetMapping(path = "/api/manager")
    public String manager(){
        return "manager";
    }
}
