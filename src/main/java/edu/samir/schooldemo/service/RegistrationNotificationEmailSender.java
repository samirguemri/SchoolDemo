package edu.samir.schooldemo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


@Service
public class RegistrationNotificationEmailSender {

    private static final int EXPIRATION = 60 * 24;

    private final MessageSource messages;
    private final JavaMailSender mailSender;
    private final Environment springEnvironment;
    private RegistrationAuthenticationEvent registrationEvent;

    @Autowired
    public RegistrationNotificationEmailSender(MessageSource messages, JavaMailSender mailSender, Environment springEnvironment) {
        this.messages = messages;
        this.mailSender = mailSender;
        this.springEnvironment = springEnvironment;
    }

    public void confirmRegistration(RegistrationAuthenticationEvent registrationEvent){

        this.registrationEvent = registrationEvent;
        String emailString = registrationEvent.getUserDto().getEmail();
        Date expiryTime = calculateExpiryDate(EXPIRATION);

        String stringToEncode = new StringBuilder()
                                    .append("email=")
                                    .append(emailString)
                                    .append("&")
                                    .append("expiry=")
                                    .append(DateConverter.dateToString(expiryTime))
                                    .toString();

        String encodedPath = PathEncoder.encode(stringToEncode);

        final SimpleMailMessage registrationEmail = constructEmailMessage(registrationEvent, encodedPath);

        mailSender.send(registrationEmail);

    }

    public boolean validateRegistration(String encodedPath){

        String path = PathEncoder.decode(encodedPath);

        String email = path.split("&")[0].split("=")[1];
        String expirationTime = path.split("&")[1].split("=")[1];
        boolean validRegistrationLink = verifyRegistrationExpiration(expirationTime);

        if ( registrationEvent.getUserDto().getEmail().equals(email) && validRegistrationLink ){
            return true;
        }
        return false;
    }

    private boolean verifyRegistrationExpiration(String expirationTime) {
        Date expirationDate = DateConverter.stringToDate(expirationTime);
        return expirationDate.compareTo(Date.from(Instant.now())) >= 0;
    }

    public void notifyAuthentication(){

    }

    private Date calculateExpiryDate(final int expiryTimeInMinutes) {

        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Date(calendar.getTime().getTime());

    }

    private SimpleMailMessage constructEmailMessage(final RegistrationAuthenticationEvent event, final String path) {

        final String recipientEmail = event.getUserDto().getEmail();
        final String subject = "Registration Confirmation";

        final String message = messages.getMessage("message.regSuccLink",
                                                    null,
                                                    "You registered successfully. To confirm your registration, please click on the below link.",
                                                    Locale.FRANCE);
        final String confirmationUrl = new StringBuilder()
                .append(event.getUrl())
                .append("registrationConfirm/")
                .append(path)
                .toString();

        final SimpleMailMessage email = new SimpleMailMessage();
        email.setFrom(springEnvironment.getProperty("app.email.support"));
        email.setTo(recipientEmail);
        email.setSubject(subject);
        email.setText(message + " \r\n" + confirmationUrl);

        return email;
    }

    static class PathEncoder {

        public static String encode(String stringToEncode){
            return Base64.getEncoder().encodeToString(stringToEncode.getBytes());
        }

        public static String decode(String encodedPath){
            return new String(Base64.getDecoder().decode(encodedPath));
        }

    }

    static class DateConverter{

        public static String dateToString(Date date){
            return String.valueOf(date.getTime());
        }

        public static Date stringToDate(String timeAsString){

            long time = Long.valueOf(timeAsString);
            Date date = new Date();
            date.setTime(time);
            return date;

        }
    }
}
