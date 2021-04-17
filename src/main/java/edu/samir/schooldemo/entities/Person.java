package edu.samir.schooldemo.entities;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDate;

@FieldDefaults(level= AccessLevel.PROTECTED)
@NoArgsConstructor
@Data
@MappedSuperclass
public abstract class Person {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    @SequenceGenerator(name = "PERSON_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PERSON_SEQ")
    Long id;

    @Column(name = "first_name")
    String firstName;

    @Column(name = "last_name")
    String lastName;

    @Column(updatable = false, unique = true)
    String email;

    @Column(columnDefinition = "DATE")
    LocalDate birthday;

    @Column(nullable = false)
    Integer age;

    public Person(String firstName, String lastName, String email, LocalDate birthday, Integer age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.birthday = birthday;
        this.age = age;
    }
}
