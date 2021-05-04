package edu.samir.schooldemo.service.registration.event;

import edu.samir.schooldemo.service.email.EmailService;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class RegistrationEventListener implements ApplicationListener<RegistrationEvent> {

    private final EmailService emailService;

    @Override
    public void onApplicationEvent(RegistrationEvent registrationEvent) {
        emailService.send(registrationEvent.getEmail(), registrationEvent.getEmailBody());
    }
}
