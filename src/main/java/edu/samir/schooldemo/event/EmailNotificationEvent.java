package edu.samir.schooldemo.event;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.servlet.http.HttpServletRequest;

public class EmailNotificationEvent {

    private final String email;
    private final String url;

    public EmailNotificationEvent(HttpServletRequest request, String email) {
        this.email = email;
        this.url = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getRequestURI();
    }

    public String getUrl() {
        return url;
    }

    public String getEmail() {
        return email;
    }

}
