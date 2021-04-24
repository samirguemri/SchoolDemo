package edu.samir.schooldemo.service;

import edu.samir.schooldemo.controller.dto.UserDto;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;

public class RegistrationAuthenticationEvent {

    private final UserDto userDto;
    private HttpServletRequest request;
    private String url;
    private String ip;


    public RegistrationAuthenticationEvent(UserDto userDto, HttpServletRequest request) {
        this.userDto = userDto;
        this.request = request;
        this.init();
    }

    public UserDto getUserDto() {
        return userDto;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public String getUrl() {
        return url;
    }

    public String getIp() {
        return ip;
    }

    private void init() {
        this.url = getAppUrl();
        this.ip = getClientIP();
    }

    private String getAppUrl() {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getRequestURI();
    }

    private String getClientIP() {
        final String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
}
