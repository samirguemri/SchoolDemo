package edu.samir.schooldemo.entities;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDate;

@FieldDefaults(level= AccessLevel.PRIVATE)
@NoArgsConstructor
@Data
@Entity
@Table(
        name = "ADMINISTRATOR",
        uniqueConstraints =
                @UniqueConstraint(
                        name = "ADMIN_CONSTRAINTS",
                        columnNames = {"email","username"}
                )
)
public class Administrator extends Person{

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    Role role;

    public Administrator(String firstName, String lastName, String email, LocalDate birthday, String username, String password, Role role) {
        super(firstName, lastName, email, birthday, LocalDate.now().getYear() - birthday.getYear(), username, password);
        this.role = role;
    }
}
