package edu.samir.schooldemo.persistence.repository;

import edu.samir.schooldemo.service.authentication.otp.Otp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OptRepository extends JpaRepository<Otp, Long> {

    Optional<Otp> findOptByUsername(String username);
}
