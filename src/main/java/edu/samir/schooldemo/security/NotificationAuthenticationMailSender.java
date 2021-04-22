package edu.samir.schooldemo.security;

import edu.samir.schooldemo.persistence.entity.User;
import edu.samir.schooldemo.security.authentication.UsernamePasswordAuthentication;
import edu.samir.schooldemo.security.model.MyUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.provisioning.UserDetailsManager;

public class NotificationAuthenticationMailSender {

    @Autowired
    private UserDetailsManager userDetailsManager;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private Environment environment;

    private void confirmStep1Authentication(UsernamePasswordAuthentication auth) {

        String  username = auth.getName();
        User user = ( (MyUserDetails) userDetailsManager.loadUserByUsername(username) ).getUser();

        String email = user.getEmail();
/*
        final User user = event.getUser();
        final String token = UUID.randomUUID().toString();
        service.createVerificationTokenForUser(user, token);

        final SimpleMailMessage emailMessage = constructEmailMessage(event, user, token);
        mailSender.send(emailMessage);
    }

    private SimpleMailMessage constructEmailMessage(final OnRegistrationCompleteEvent event, final User user, final String token) {
        final String recipientAddress = user.getEmail();
        final String subject = "Registration Confirmation";
        final String confirmationUrl = event.getAppUrl() + "/registrationConfirm.html?token=" + token;
        final String message = messages.getMessage("message.regSuccLink", null, "You registered successfully. To confirm your registration, please click on the below link.", event.getLocale());
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message + " \r\n" + confirmationUrl);
        email.setFrom(env.getProperty("support.email"));
        return email;
 */
    }
}
