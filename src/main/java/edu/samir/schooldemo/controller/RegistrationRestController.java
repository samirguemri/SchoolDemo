package edu.samir.schooldemo.controller;

import edu.samir.schooldemo.controller.dto.UserDto;
import edu.samir.schooldemo.persistence.entity.User;
import edu.samir.schooldemo.service.RegistrationAuthenticationEvent;
import edu.samir.schooldemo.service.RegistrationNotificationEmailSender;
import edu.samir.schooldemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.net.URI;

@RestController
public class RegistrationRestController {

    private UserDto userDto;
    private final UserService userService;
    private final RegistrationNotificationEmailSender emailSender;
    private RegistrationAuthenticationEvent registrationEvent;

    @Autowired
    public RegistrationRestController(UserService userService, RegistrationNotificationEmailSender emailSender) {
        this.userService = userService;
        this.emailSender = emailSender;
    }

    @PostMapping(
            path = "/api/user/registration",
            consumes = "application/json"
    )
    public ResponseEntity<UserDto> registerNewUser(final HttpServletRequest request, @NotNull @RequestBody UserDto userDto){

        if (userDto == null)
            return ResponseEntity.noContent().build();
        this.userDto = userDto;

        // before adding, the user should confirm registration
        registrationEvent = new RegistrationAuthenticationEvent(userDto, request);
        emailSender.confirmRegistration(registrationEvent);

        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/api/user/registration/registrationConfirm/{registrationToken}")
    public ResponseEntity<User> validateRegistration(final HttpServletRequest request, @PathVariable String registrationToken){

        registrationEvent.setRequest(request);
        boolean validaRegistration = emailSender.validateRegistration(registrationToken);

        if ( !validaRegistration ){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }

        User newUser = userService.insertNewUser(new User(userDto));

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .replacePath("/api/student")
                .path("/{id}")
                .buildAndExpand(newUser.getId())
                .toUri();
        return ResponseEntity.created(location).build();

    }

    private String extractPath(String currentRequestPath) {
        int index = currentRequestPath.lastIndexOf("/");
        return currentRequestPath.substring(0, index);
    }
 }
