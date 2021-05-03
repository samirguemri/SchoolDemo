package edu.samir.schooldemo.service;

import edu.samir.schooldemo.service.registration.event.RegistrationEvent;
import edu.samir.schooldemo.service.authentication.otp.Otp;
import edu.samir.schooldemo.service.authentication.otp.OptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;


@Service
public class EmailNotificationEventService {

    private static final int ONE_DAY_EXPIRATION = 60 * 24;
    private static final int FIVE_MINUTES_EXPIRATION = 5;

    private final OptRepository optRepository;
    private final MessageSource messages;
    private final JavaMailSender mailSender;
    private final Environment springEnvironment;
    private RegistrationEvent event;

    @Autowired
    public EmailNotificationEventService(OptRepository optRepository,
                                         MessageSource messages,
                                         JavaMailSender mailSender,
                                         Environment springEnvironment) {
        this.optRepository = optRepository;
        this.messages = messages;
        this.mailSender = mailSender;
        this.springEnvironment = springEnvironment;
    }

    public void confirmRegistration(RegistrationEvent registrationEvent) {

        this.event = registrationEvent;
        String emailString = registrationEvent.getEmail();
        Date expiryTime = calculateExpiryDate(ONE_DAY_EXPIRATION);

        String stringToEncode = new StringBuilder()
                                    .append("email=")
                                    .append(emailString)
                                    .append("&")
                                    .append("expiry=")
                                    .append(DateConverter.dateToString(expiryTime))
                                    .toString();

        String encodedPath = PathEncoder.encode(stringToEncode);
        final String confirmationUrl = new StringBuilder()
                //.append(event.getUrl())
                .append("confirmRegistration/")
                .append(encodedPath)
                .toString();

        final SimpleMailMessage registrationEmail = constructRegistrationEmailMessage(this.event, confirmationUrl);

        mailSender.send(registrationEmail);
    }

    public boolean validateRegistration(String encodedPath){

        String path = PathEncoder.decode(encodedPath);

        String email = path.split("&")[0].split("=")[1];
        String expirationTime = path.split("&")[1].split("=")[1];
        boolean validRegistrationLink = checkRegistrationExpiration(expirationTime);

        return validRegistrationLink && event.getEmail().equals(email);
    }

    private SimpleMailMessage constructRegistrationEmailMessage(final RegistrationEvent registrationEvent, final String confirmationUrl) {

        final String recipientEmail = registrationEvent.getEmail();
        final String subject = "Registration Confirmation";

        final String message = messages.getMessage("message.regSuccLink",
                null,
                "You registered successfully. To confirm your registration, please click on the below link.",
                Locale.FRANCE);

        final SimpleMailMessage email = new SimpleMailMessage();
        email.setFrom(springEnvironment.getProperty("app.email.support"));
        email.setTo(recipientEmail);
        email.setSubject(subject);
        email.setText(message + " \n\n\t" + confirmationUrl);

        return email;
    }

    public void notifyAuthentication(RegistrationEvent authenticationEvent){

        String email = authenticationEvent.getEmail();
        Date expiryTime = calculateExpiryDate(FIVE_MINUTES_EXPIRATION);
        String otp = generateRandomOtp();

        final String confirmationUrl = new StringBuilder()
                //.append(authenticationEvent.getUrl())
                .append("confirmAuthentication/")
                //.append(encodedPath)
                .toString();

        final SimpleMailMessage authenticationEmail = constructAuthenticationEmailMessage(authenticationEvent, confirmationUrl, otp);

        mailSender.send(authenticationEmail);
        optRepository.save(new Otp(email, otp));
    }

/*
    public boolean validateAuthentication(String encodedPath){

        String path = PathEncoder.decode(encodedPath);

        String email = path.split("&")[0].split("=")[1];
        String otp = path.split("&")[1].split("=")[1];
        String expirationTime = path.split("&")[2].split("=")[1];

        boolean validAuthenticationLink = verifyRegistrationExpiration(expirationTime);

        return validAuthenticationLink && event.getEmail().equals(email);
    }
*/

    private SimpleMailMessage constructAuthenticationEmailMessage(final RegistrationEvent registrationEvent,
                                                                  final String confirmationUrl,
                                                                  final String otp) {

        final String recipientEmail = registrationEvent.getEmail();
        final String subject = "Authentication Confirmation";

        final String message = messages.getMessage("message.regSuccLink",
                null,
                "To confirm your authentication, please click on the below link and insert the given Otp Token.",
                Locale.FRANCE);

        final SimpleMailMessage email = new SimpleMailMessage();
        email.setFrom(springEnvironment.getProperty("app.email.support"));
        email.setTo(recipientEmail);
        email.setSubject(subject);
        email.setText(message + " \n\n\t " + confirmationUrl + "\n\n\t\t\t\t" + otp);

        return email;
    }

    private boolean checkRegistrationExpiration(String expirationTime) {
        Date expirationDate = DateConverter.stringToDate(expirationTime);
        return expirationDate.compareTo(Date.from(Instant.now())) >= 0;
    }


    private String generateRandomOtp() {
        Random rnd = new Random();
        char[] array = new char[5];
        for (int i = 0, l = array.length; i < l; i++) {
            array[i] = (char) ('A' + rnd.nextInt(26));
        }
        return new String(array);
    }

    private Date calculateExpiryDate(int expiryTimeInMinutes) {

        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Date(calendar.getTime().getTime());

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

            long time = Long.parseLong(timeAsString);
            Date date = new Date();
            date.setTime(time);
            return date;

        }
    }
}
