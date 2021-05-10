package edu.samir.schooldemo.service.registration;

import edu.samir.schooldemo.exception.EmailNotValidException;
import edu.samir.schooldemo.exception.TokenNotFoundException;
import edu.samir.schooldemo.exception.UserNotFoundException;
import edu.samir.schooldemo.persistence.entity.UserEntity;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;

public interface RegistrationService {
    @Transactional
    UserEntity registerNewUser(@NotNull String url, @NotNull UserEntity userEntity) throws EmailNotValidException;

    String reSendConfirmationToken(@NotNull String url, @NotNull String username);

    @Transactional
    String confirmToken(String token) throws TokenNotFoundException, UserNotFoundException;
}
