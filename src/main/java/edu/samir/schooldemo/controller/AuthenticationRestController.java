package edu.samir.schooldemo.controller;

import edu.samir.schooldemo.persistence.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationRestController {

    @PostMapping("/confirmAuthentication/{otp}")
    public @ResponseBody ResponseEntity<User> registerNewUser(@PathVariable String otp){
        return null;
    }
}
