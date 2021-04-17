package edu.samir.schooldemo.exception;

public class UserNotFoundException extends Throwable {

    public UserNotFoundException(String msg) {
        super(msg);
    }
}
