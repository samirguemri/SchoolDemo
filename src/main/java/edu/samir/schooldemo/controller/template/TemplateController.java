package edu.samir.schooldemo.controller.template;

import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collection;
import java.util.stream.Collectors;

@AllArgsConstructor
@Controller
public class TemplateController {

    @GetMapping("/login")
    public String getLoginView(){
        return "login";
    }

    @GetMapping("/userhomepage")
    public String getRoleHomePageView(Authentication authentication){
        // redirect user according to his ROLE
        Collection<String> roles = authentication.getAuthorities()
                                    .stream()
                                    .map(authority -> authority.getAuthority())
                                    .collect(Collectors.toSet());
        if(roles.contains("ROLE_MANAGER"))
            return "manager/home";
        if(roles.contains("ROLE_ADMIN"))
            return "admin/home";
        if(roles.contains("ROLE_TEACHER"))
            return "teacher/home";
        return "student/home";
    }
}
