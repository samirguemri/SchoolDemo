package edu.samir.schooldemo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EmailNotValidException extends Throwable {
    public EmailNotValidException(String message) {
        super(message);
    }
}
