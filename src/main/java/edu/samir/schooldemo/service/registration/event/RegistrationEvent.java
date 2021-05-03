package edu.samir.schooldemo.service.registration.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class RegistrationEvent extends ApplicationEvent {

    private final String email;
    private final String registrationLink;

    public RegistrationEvent(Object source, String registrationLink, String email) {
        super(source);
        this.email = email;
        this.registrationLink = registrationLink;
    }

}
