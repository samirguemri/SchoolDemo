package edu.samir.schooldemo.persistence.repository;

import edu.samir.schooldemo.persistence.entity.UserEntity;
import edu.samir.schooldemo.service.registration.token.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {

    Optional<ConfirmationToken> findByUser(UserEntity entity);

    Optional<ConfirmationToken> findByToken(String token);
}
