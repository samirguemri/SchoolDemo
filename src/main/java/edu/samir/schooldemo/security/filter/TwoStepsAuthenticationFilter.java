package edu.samir.schooldemo.security.filter;

import edu.samir.schooldemo.security.authentication.EmailNotificationAuthentication;
import edu.samir.schooldemo.security.authentication.UsernamePasswordAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Random;

@Component("2StepsAuthenticationFilter")
public class TwoStepsAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        // 2 steps Authentication
        // step1 : using username & password
        // step2 : using email & random key

        String username = httpServletRequest.getHeader("username");
        String password = httpServletRequest.getHeader("password");
        String email = httpServletRequest.getHeader("email");

        if ( email == null ) { // we are still step1

            UsernamePasswordAuthentication usernamePasswordAuthentication = new UsernamePasswordAuthentication(username, password);
            doAuthentication(usernamePasswordAuthentication, httpServletResponse);

            // at this stage : we are sure that the authentication is OK
            // so we can go to step2
            // but before we should send the notificationAuthenticationMail

        } else { // we are basically in step2
            String randomKey = generateRandomKey();
            EmailNotificationAuthentication emailNotificationAuthentication = new EmailNotificationAuthentication(email, randomKey);
            doAuthentication(emailNotificationAuthentication, httpServletResponse);
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private String generateRandomKey() {
        byte[] array = new byte[5]; // length is bounded by 7
        new Random().nextBytes(array);
        return new String(array, Charset.forName("UTF-8"));
    }

    private void doAuthentication(Authentication authentication, HttpServletResponse response){
        try {
            Authentication authenticationResult = authenticationManager.authenticate(authentication);

            if (authenticationResult.isAuthenticated()){
                SecurityContextHolder.getContext().setAuthentication(authenticationResult);
            } else {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            }
        } catch (AuthenticationException exception) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }
    }

}
