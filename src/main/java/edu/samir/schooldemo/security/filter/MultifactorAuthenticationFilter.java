package edu.samir.schooldemo.security.filter;

import edu.samir.schooldemo.security.authentication.OtpAuthentication;
import edu.samir.schooldemo.security.authentication.UsernamePasswordAuthentication;
import edu.samir.schooldemo.security.manager.TokenManager;
import edu.samir.schooldemo.security.model.SecurityUser;
import edu.samir.schooldemo.service.EmailNotificationEventService;
import edu.samir.schooldemo.service.authentication.otp.Otp;
import edu.samir.schooldemo.service.authentication.otp.OtpService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Random;
import java.util.UUID;

public class MultifactorAuthenticationFilter extends OncePerRequestFilter {

    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private TokenManager tokenManager;
    @Autowired private OtpService otpService;

    @Override
    public void doFilterInternal(HttpServletRequest request,
                                 HttpServletResponse response,
                                 FilterChain filterChain) throws IOException, ServletException {

        String username = request.getHeader("username");
        String password = request.getHeader("password");
        String otp = request.getHeader("otp");

        if (otp == null) {
            // step 1
            UsernamePasswordAuthentication auth = new UsernamePasswordAuthentication(username, password);
            Authentication authentication = authenticationManager.authenticate(auth);
            if (authentication.isAuthenticated()) {
                //this.emailSenderService.notifyAuthentication(new RegistrationEvent(request, username));
                String otpString = generateRandomOtp();
                otpService.saveOtp(new Otp(username, otpString));
                // TODO: solve/update authentication event
            }
        } else {
            // step 2
            Authentication authentication = this.authenticationManager.authenticate(new OtpAuthentication(username, otp));
            if (authentication.isAuthenticated()) { // if the token is authenticated

                String token = UUID.randomUUID().toString();
                this.tokenManager.addToken(token);
                response.setHeader("Authorization", token);
            }
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        // to avoid filtering of the given request
        // !(should NOT Filter those reqPath) => the filter would be enabled for this reqPath
        return !request.getServletPath().equals("/login");
    }

    private String generateRandomOtp() {
        Random rnd = new Random();
        char[] array = new char[5];
        for (int i = 0, l = array.length; i < l; i++) {
            array[i] = (char) ('A' + rnd.nextInt(26));
        }
        return new String(array);
    }
}
