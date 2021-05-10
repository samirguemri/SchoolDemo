package edu.samir.schooldemo.service.registration;

import edu.samir.schooldemo.exception.EmailNotValidException;
import edu.samir.schooldemo.exception.TokenNotFoundException;
import edu.samir.schooldemo.exception.UserNotFoundException;
import edu.samir.schooldemo.persistence.entity.UserEntity;
import edu.samir.schooldemo.security.userdetail.CustomUserDetails;
import edu.samir.schooldemo.security.userdetail.CustomUserDetailsManager;
import edu.samir.schooldemo.service.registration.event.RegistrationEventPublisher;
import edu.samir.schooldemo.service.registration.token.ConfirmationToken;
import edu.samir.schooldemo.service.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@Service
public class RegistrationServiceImpl implements RegistrationService {

    private final CustomUserDetailsManager userDetailsManager;
    private final EmailValidator emailValidator;
    private final RegistrationEventPublisher registrationEventPublisher;
    private final ConfirmationTokenService confirmationTokenService;

    @Override
    @Transactional
    public UserEntity registerNewUser(@NotNull final String url, @NotNull final UserEntity userEntity) throws EmailNotValidException {

        boolean isValidEmail = this.emailValidator.test(userEntity.getEmail());
        if (!isValidEmail) {
            throw new EmailNotValidException(String.format("email %s is NOT valid", userEntity.getEmail()));
        }

        ConfirmationToken confirmationToken = this.createRandomConfirmationToken(userEntity);

        // Create the User
        userDetailsManager.createUser(new CustomUserDetails(userEntity));
        // Generate and save random Token
        confirmationTokenService.saveToken(confirmationToken);
        // Email the Token
        registrationEventPublisher.publishRegistrationEvent(userEntity, confirmationToken.getToken(), url);

        return userEntity;
    }

    @Override
    public String reSendConfirmationToken(@NotNull final String url, @NotNull final String username){

        UserEntity userEntity = ((CustomUserDetails) userDetailsManager.loadUserByUsername(username)).getUserEntity();
        ConfirmationToken confirmationToken = this.createRandomConfirmationToken(userEntity);

        // Generate and save random Token
        confirmationTokenService.saveToken(confirmationToken);
        // Send the Token
        registrationEventPublisher.publishRegistrationEvent(userEntity, confirmationToken.getToken(), url);

        return "token sent";
    }

    @Override
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

    private ConfirmationToken createRandomConfirmationToken(UserEntity userEntity) {
        String token = UUID.randomUUID().toString();
        return new ConfirmationToken(token, LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), userEntity);
    }
}
