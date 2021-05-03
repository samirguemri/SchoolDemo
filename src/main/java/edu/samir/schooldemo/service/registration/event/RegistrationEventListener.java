package edu.samir.schooldemo.service.registration.event;

import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Locale;

@AllArgsConstructor
@Service
public class RegistrationEventListener implements ApplicationListener<RegistrationEvent> {

    private final Environment springEnvironment;
    private final JavaMailSender mailSender;
    private final MessageSource messages;

    @Override
    public void onApplicationEvent(RegistrationEvent registrationEvent) {

        final SimpleMailMessage registrationEmail = constructRegistrationEmailMessage(registrationEvent);

        this.mailSender.send(registrationEmail);
    }

    private SimpleMailMessage constructRegistrationEmailMessage(final RegistrationEvent registrationEvent) {

        final String sender = this.springEnvironment.getProperty("app.email.support");
        final String recipient = registrationEvent.getEmail();
        final String subject = "Registration Confirmation";

        final String message = this.messages.getMessage("message.regSuccLink",
                null,
                "You registered successfully. To confirm your registration, please click on the below link.",
                Locale.FRANCE);

        final SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

        simpleMailMessage.setFrom(sender);
        simpleMailMessage.setTo(recipient);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(message + " \n\n\t" + registrationEvent.getRegistrationLink());

        return simpleMailMessage;
    }
}
