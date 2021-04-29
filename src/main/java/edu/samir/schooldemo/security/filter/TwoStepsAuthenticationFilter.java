package edu.samir.schooldemo.security.filter;

import edu.samir.schooldemo.event.EmailNotificationEvent;
import edu.samir.schooldemo.security.authentication.OtpAuthentication;
import edu.samir.schooldemo.security.authentication.UsernamePasswordAuthentication;
import edu.samir.schooldemo.service.EmailNotificationEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class TwoStepsAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private EmailNotificationEventService emailSenderService;

    @Override
    public void doFilterInternal(HttpServletRequest request,
                                 HttpServletResponse response,
                                 FilterChain filterChain) throws IOException, ServletException {

        String username = request.getHeader("username");
        String password = request.getHeader("password");
        String otp = request.getHeader("otp");

        if (otp == null) {
            // step 1
            Authentication authentication = this.authenticationManager.authenticate(new UsernamePasswordAuthentication(username, password));

            if (authentication.isAuthenticated()){ // if the token is authenticated

                // Add the authenticateResult to the Spring Security Context
                SecurityContextHolder.getContext().setAuthentication(authentication);

                // send Otp token through email notification
                this.emailSenderService.notifyAuthentication(new EmailNotificationEvent(request, username));
            } else {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            }

        } else {
            // step 2
            Authentication authentication = this.authenticationManager.authenticate(new OtpAuthentication(username, otp));
            if (authentication.isAuthenticated()){ // if the token is authenticated

                // Add the authenticateResult to the Spring Security Context
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            }
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        // to avoid filtering of the given request
        // !(should NOT Filter those reqPath) => the filter would be enabled for those reqPath
        //return !request.getServletPath().equals("/login") || !request.getServletPath().equals("/secure");
        return super.shouldNotFilter(request);
    }

}
