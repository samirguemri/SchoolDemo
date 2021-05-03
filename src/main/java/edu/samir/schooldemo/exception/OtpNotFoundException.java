package edu.samir.schooldemo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class OtpNotFoundException extends Throwable{
    public OtpNotFoundException(String message) {
        super(message);
    }
}
