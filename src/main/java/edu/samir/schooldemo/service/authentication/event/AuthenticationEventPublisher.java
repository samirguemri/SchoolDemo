package edu.samir.schooldemo.service.authentication.event;

import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service("authentication")
public class AuthenticationEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public void publishAuthenticationEvent(){
        AuthenticationEvent authenticationEvent = new AuthenticationEvent(this);
        this.applicationEventPublisher.publishEvent(authenticationEvent);
    }
}
