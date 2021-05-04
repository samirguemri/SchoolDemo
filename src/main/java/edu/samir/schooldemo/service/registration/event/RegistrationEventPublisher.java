package edu.samir.schooldemo.service.registration.event;

import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Service("registration")
public class RegistrationEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public void publishRegistrationEvent(String email, String token, @NotNull String path) {
        String registrationLink = path + "/confirm?token=" + token;
        RegistrationEvent registrationEvent = new RegistrationEvent(this, registrationLink, email);
        this.applicationEventPublisher.publishEvent(registrationEvent);
    }
}
