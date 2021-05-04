package edu.samir.schooldemo.service.registration.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class RegistrationEvent extends ApplicationEvent {

    private final String email;
    private final String emailBody;

    public RegistrationEvent(Object source, String email, String emailBody) {
        super(source);
        this.email = email;
        this.emailBody = emailBody;
    }

}
