package edu.samir.schooldemo.service.registration;

import edu.samir.schooldemo.controller.dto.UserDto;
import edu.samir.schooldemo.exception.EmailNotValidException;
import edu.samir.schooldemo.exception.TokenNotFoundException;
import edu.samir.schooldemo.exception.UserNotFoundException;
import edu.samir.schooldemo.persistence.entity.UserEntity;
import edu.samir.schooldemo.security.JpaUserDetailsManager;
import edu.samir.schooldemo.security.model.SecurityUser;
import edu.samir.schooldemo.service.registration.event.RegistrationEventPublisher;
import edu.samir.schooldemo.service.registration.token.ConfirmationToken;
import edu.samir.schooldemo.service.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@Service
public class RegistrationService {

    private final JpaUserDetailsManager userDetailsManager;
    private final PasswordEncoder passwordEncoder;
    private final EmailValidator emailValidator;
    private final RegistrationEventPublisher registrationEventPublisher;
    private final ConfirmationTokenService confirmationTokenService;

    @Transactional
    public UserEntity registerNewUser(@NotNull final String path, @NotNull final UserDto userDto) throws EmailNotValidException {

        boolean isValidEmail = this.emailValidator.test(userDto.getEmail());
        if (!isValidEmail) {
            throw new EmailNotValidException(String.format("email %s is NOT valid",userDto.getEmail()));
        }

        UserEntity userEntity = populateUserEntity(userDto);
        ConfirmationToken confirmationToken = this.createRandomConfirmationToken(userEntity);

        // Create the User
        userDetailsManager.createUser(new SecurityUser(userEntity));
        // Generate and save random Token
        confirmationTokenService.saveToken(confirmationToken);
        // Email the Token
        registrationEventPublisher.publishRegistrationEvent(userDto.getEmail(), confirmationToken.getToken(), path);

        return userEntity;
    }

    public String reSendConfirmationToken(@NotNull final String path, @NotNull final String username){

        UserEntity userEntity = ((SecurityUser) userDetailsManager.loadUserByUsername(username)).getUserEntity();
        ConfirmationToken confirmationToken = this.createRandomConfirmationToken(userEntity);

        // Generate and save random Token
        confirmationTokenService.saveToken(confirmationToken);
        // Send the Token
        registrationEventPublisher.publishRegistrationEvent(confirmationToken.getToken(), userEntity.getEmail(), path);

        return "token sent";
    }

    @Transactional
    public String confirmToken(String token) throws TokenNotFoundException, UserNotFoundException {

        ConfirmationToken confirmationToken = confirmationTokenService.getToken(token)
                                                                        .orElseThrow(() -> new TokenNotFoundException("the given token does NOT exists !"));

        if (confirmationToken.getConfirmedAt() != null){
            throw new IllegalStateException("you are already confirmed !");
        }

        if (LocalDateTime.now().isAfter(confirmationToken.getExpiresAt())) {
            throw new IllegalStateException("the confirmation token was expired !");
        }

        confirmationTokenService.confirmToken(confirmationToken);
        userDetailsManager.enableUser(confirmationToken.getUser());

        return "user confirmed & enabled";
    }

    private UserEntity populateUserEntity(final UserDto userDto) {

        UserEntity userEntity = new UserEntity();

        userEntity.setFirstName(userDto.getFirstName());
        userEntity.setLastName(userDto.getLastName());
        userEntity.setEmail(userDto.getEmail());
        userEntity.setBirthday(userDto.getBirthday());
        userEntity.setAge(LocalDate.now().getYear() - userDto.getBirthday().getYear());
        userEntity.setUsername(userDto.getUsername());
        userEntity.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userEntity.setEnabled(false);

        return userEntity;
    }

    private ConfirmationToken createRandomConfirmationToken(UserEntity userEntity) {
        String token = UUID.randomUUID().toString();
        return new ConfirmationToken(token, LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), userEntity);
    }
}
