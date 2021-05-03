package edu.samir.schooldemo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TokenNotFoundException extends Throwable {
    public TokenNotFoundException(String message) {
        super(message);
    }
}
