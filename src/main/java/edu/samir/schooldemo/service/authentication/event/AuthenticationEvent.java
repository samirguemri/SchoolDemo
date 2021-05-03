package edu.samir.schooldemo.service.authentication.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class AuthenticationEvent extends ApplicationEvent {

    public AuthenticationEvent(Object source) {
        super(source);
    }
}
