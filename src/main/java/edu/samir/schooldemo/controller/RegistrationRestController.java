package edu.samir.schooldemo.controller;

import edu.samir.schooldemo.controller.dto.UserDto;
import edu.samir.schooldemo.event.EmailNotificationEvent;
import edu.samir.schooldemo.persistence.entity.User;
import edu.samir.schooldemo.service.EmailNotificationEventService;
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
    private final EmailNotificationEventService emailSenderService;
    private EmailNotificationEvent registrationEvent;

    @Autowired
    public RegistrationRestController(UserService userService, EmailNotificationEventService emailSenderService) {
        this.userService = userService;
        this.emailSenderService = emailSenderService;
    }

    @GetMapping("/")
    public String home() {
        return "Home!";
    }

    @GetMapping("/home")
    public String welcome() {
        return "Welcome Home!!";
    }

    @GetMapping("/login")
    public String securePage() {
        return "Welcome to the login Page!";
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
        registrationEvent = new EmailNotificationEvent(request, userDto.getEmail() );
        emailSenderService.confirmRegistration(registrationEvent);

        ResponseEntity<UserDto> dtoResponseEntity = ResponseEntity.ok(userDto);
        return dtoResponseEntity;
    }

    @GetMapping(path = "/api/user/registration/confirmRegistration/{registrationPath}")
    public ResponseEntity<User> validateRegistration(@PathVariable String registrationPath){

        boolean validaRegistration = emailSenderService.validateRegistration(registrationPath);

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
 }
