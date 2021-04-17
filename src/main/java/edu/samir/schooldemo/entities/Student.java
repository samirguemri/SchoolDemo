package edu.samir.schooldemo.entities;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import edu.samir.schooldemo.config.json.StudentDeserializer;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.time.LocalDate;

@FieldDefaults(level= AccessLevel.PRIVATE)
@NoArgsConstructor
@Data
@Entity
@Table(
        name = "STUDENT",
        uniqueConstraints =
                @UniqueConstraint(
                        name = "STUDENT_CONSTRAINTS",
                        columnNames = {"email","username"}
                )
)
@JsonDeserialize(using = StudentDeserializer.class)
public class Student extends Person{

    @Column(nullable = false, unique = true)
    String username;

    @Column
    String password;

    public Student(String firstName, String lastName, String email, LocalDate birthday, String username, String password) {
        super(firstName, lastName, email, birthday, LocalDate.now().getYear() - birthday.getYear());
        this.username = username;
        this.password = password;
    }
}
