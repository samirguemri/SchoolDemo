package edu.samir.schooldemo.service.authentication.event;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationEventListener implements ApplicationListener<AuthenticationEvent> {

    @Override
    public void onApplicationEvent(AuthenticationEvent authenticationEvent) {

        // TODO: implement authenticationEvent Logic
    }
}
