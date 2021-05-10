package edu.samir.schooldemo.service.authentication;

import edu.samir.schooldemo.service.authentication.event.AuthenticationEventPublisher;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AuthenticationService {

    public final AuthenticationEventPublisher authenticationEventPublisher;

    public void doAuthentication(){
        this.authenticationEventPublisher.publishAuthenticationEvent();
    }
}
