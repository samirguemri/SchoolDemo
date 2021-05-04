package edu.samir.schooldemo.controller;

import edu.samir.schooldemo.controller.dto.UserDto;
import edu.samir.schooldemo.exception.EmailNotValidException;
import edu.samir.schooldemo.exception.TokenNotFoundException;
import edu.samir.schooldemo.exception.UserNotFoundException;
import edu.samir.schooldemo.persistence.entity.UserEntity;
import edu.samir.schooldemo.service.EmailNotificationEventService;
import edu.samir.schooldemo.service.registration.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;

@RestController
public class RegistrationRestController {

    private final RegistrationService registrationService;
    private final EmailNotificationEventService emailSenderService;

    @Autowired
    public RegistrationRestController(RegistrationService registrationService, EmailNotificationEventService emailSenderService) {
        this.registrationService = registrationService;
        this.emailSenderService = emailSenderService;
    }

    @GetMapping("/secure")
    public String welcome() {
        return "This is secured endpoint !";
    }

    @PostMapping(
            path = "/api/user/registration",
            consumes = "application/json"
    )
    public ResponseEntity<UserEntity> registerNewUser(HttpServletRequest request, final @NotNull @RequestBody UserDto userDto) {

        UserEntity userEntity = null;
        if (userDto == null)
            return ResponseEntity.noContent().build();

        try {
            String path = this.buildRequestPath(request);
            userEntity = registrationService.registerNewUser(path, userDto);
        } catch (EmailNotValidException e) {
            // TODO: process exception
            e.printStackTrace();
        }
        return ResponseEntity.ok(userEntity);
    }

    private String buildRequestPath(HttpServletRequest request) {
        return UriComponentsBuilder.fromHttpRequest(new ServletServerHttpRequest(request)).build().toUriString();
    }

    @GetMapping("/api/user/registration/resend/{username}")
    public String resendConfirmationToken(HttpServletRequest request, @PathVariable String username){
        String path = this.buildRequestPath(request);
        return registrationService.reSendConfirmationToken(path, username);
    }

    @GetMapping("/api/user/registration/confirm")
    public String confirmRegistration(@RequestParam String token){
        try {
            registrationService.confirmToken(token);
        } catch (TokenNotFoundException | UserNotFoundException e) {
            // TODO: process exception
            e.printStackTrace();
            return null;
        }
        return "confirmed";
    }
}
