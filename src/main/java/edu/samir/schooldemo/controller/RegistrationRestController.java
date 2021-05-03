package edu.samir.schooldemo.controller;

import edu.samir.schooldemo.controller.dto.UserDto;
import edu.samir.schooldemo.exception.EmailNotValidException;
import edu.samir.schooldemo.persistence.entity.UserEntity;
import edu.samir.schooldemo.service.EmailNotificationEventService;
import edu.samir.schooldemo.service.registration.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/")
    public String home() {
        return "Home!";
    }

    //@Async
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
    public ResponseEntity<UserEntity> registerNewUser(final @NotNull @RequestBody UserDto userDto) {

        UserEntity userEntity = null;
        if (userDto == null)
            return ResponseEntity.noContent().build();

        try {
            userEntity = registrationService.registerNewUser(userDto);
        } catch (EmailNotValidException e) {
            // TODO: process exception
            e.printStackTrace();
        }
        return ResponseEntity.ok(userEntity);
    }

    @GetMapping("/api/user/registration/resend/{username}")
    public String resendConfirmationToken(@PathVariable String username){
        return registrationService.reSendConfirmationToken(username);
    }

/*
    @GetMapping(path = "/api/user/registration/confirmRegistration/{registrationPath}")
    public ResponseEntity<UserEntity> validateRegistration(@PathVariable String registrationPath) throws EmailNotValidException {

        boolean validaRegistration = emailSenderService.validateRegistration(registrationPath);

        if ( !validaRegistration ){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }

        UserEntity newUserEntity = registrationService.registerNewUser(userDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .replacePath("/api/student")
                .path("/{id}")
                .buildAndExpand(newUserEntity.getId())
                .toUri();
        return ResponseEntity.created(location).build();

    }
*/
}
