package edu.samir.schooldemo.service.registration.token;

import edu.samir.schooldemo.persistence.repository.ConfirmationTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@AllArgsConstructor
@Service
public class ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;

    public void saveToken(ConfirmationToken token){
        confirmationTokenRepository.save(token);
    }

    public Optional<ConfirmationToken> getToken(String token) {
        return confirmationTokenRepository.findByToken(token);
    }

    public void confirmToken(ConfirmationToken token){
        token.setConfirmedAt(LocalDateTime.now());
        confirmationTokenRepository.save(token);
    }
}
