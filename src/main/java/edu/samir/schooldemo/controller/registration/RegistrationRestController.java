package edu.samir.schooldemo.controller.registration;

import edu.samir.schooldemo.controller.dto.UserConverter;
import edu.samir.schooldemo.controller.dto.UserDto;
import edu.samir.schooldemo.exception.EmailNotValidException;
import edu.samir.schooldemo.exception.TokenNotFoundException;
import edu.samir.schooldemo.exception.UserNotFoundException;
import edu.samir.schooldemo.persistence.entity.UserEntity;
import edu.samir.schooldemo.service.registration.RegistrationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@RestController
public class RegistrationRestController {

    private final RegistrationService registrationService;
    private final UserConverter userConverter;

    @PostMapping(
            path = "/api/user/registration",
            consumes = "application/json"
    )
    public ResponseEntity<UserEntity> registerNewUser(HttpServletRequest request, final @NotNull @RequestBody UserDto userDto) {

        UserEntity userEntity = null;
        if (userDto == null)
            return ResponseEntity.noContent().build();
        try {
            String url = this.getAppUrl(request);
            userEntity = registrationService.registerNewUser(url, userConverter.dtoToEntity(userDto));
        } catch (EmailNotValidException e) {
            // TODO: process exception
            e.printStackTrace();
        }
        return ResponseEntity.ok(userEntity);
    }

    @GetMapping("/api/user/registration/resend/{username}")
    public String resendConfirmationToken(HttpServletRequest request, @PathVariable String username){
        String url = this.getAppUrl(request);
        return registrationService.reSendConfirmationToken(url, username);
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

    private String getAppUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort();
    }

    private String getAppUri(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }
}
