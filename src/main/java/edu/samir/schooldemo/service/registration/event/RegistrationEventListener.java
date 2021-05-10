package edu.samir.schooldemo.service.registration.event;

import edu.samir.schooldemo.service.email.EmailSender;
import edu.samir.schooldemo.service.email.EmailSenderImpl;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class RegistrationEventListener implements ApplicationListener<RegistrationEvent> {

    private final EmailSender emailSender;

    @Override
    public void onApplicationEvent(RegistrationEvent registrationEvent) {
        emailSender.send(registrationEvent.getEmail(), registrationEvent.getEmailBody());
    }
}
