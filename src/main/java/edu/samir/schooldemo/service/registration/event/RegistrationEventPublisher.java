package edu.samir.schooldemo.service.registration.event;

import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service("registration")
public class RegistrationEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public void publishRegistrationEvent(String registrationLink, String email) {
        RegistrationEvent registrationEvent = new RegistrationEvent(this, registrationLink, email);
        this.applicationEventPublisher.publishEvent(registrationEvent);
    }
}
