package edu.samir.schooldemo.persistence.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDateTime;

@FieldDefaults(level= AccessLevel.PRIVATE)
@NoArgsConstructor
@Data
@Entity
public class Otp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(unique = true, nullable = false)
    String username;

    @Column
    String otp;

    @Column
    LocalDateTime validity;

    public Otp(String username, String otp) {
        this.username = username;
        this.otp = otp;
        this.validity = LocalDateTime.now().plusMinutes(5);
    }
}
