package edu.samir.schooldemo.persistence.entity;

import edu.samir.schooldemo.controller.dto.UserDto;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@FieldDefaults(level= AccessLevel.PRIVATE)
@NoArgsConstructor
@Data
@Entity
@Table(
        name = "user_account",
        uniqueConstraints =
        @UniqueConstraint(
                name = "USER_CONSTRAINTS",
                columnNames = {"email","username"}
        )
)
public class User {

    @Id
    @Column(updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "first_name")
    String firstName;

    @Column(name = "last_name")
    String lastName;

    @Column(updatable = false)
    String email;

    @Column(columnDefinition = "DATE")
    LocalDate birthday;

    @Column(nullable = false)
    Integer age;

    @Column
    String username;

    @Column(length = 60)
    String password;

    @Column(columnDefinition = "BOOLEAN")
    boolean enabled;

    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(
            name = "user_role_association",
            joinColumns = @JoinColumn(name = "userId", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "roleId", referencedColumnName = "id"),
            foreignKey = @ForeignKey(name = "USER_FOREIGN_KEY_CONSTRAINTS"),
            inverseForeignKey = @ForeignKey(name = "ROLE_FOREIGN_KEY_CONSTRAINTS")
    )
    @Column
    Set<Role> roles;

    public User(UserDto userDto) {
        this.firstName = userDto.getFirstName();
        this.lastName = userDto.getLastName();
        this.email = userDto.getEmail();
        this.birthday = userDto.getBirthday();
        this.age = LocalDate.now().getYear() - userDto.getBirthday().getYear();
        this.username = userDto.getUsername();
        this.password = userDto.getPassword();
        this.enabled = false;
    }

    public User(String firstName, String lastName, String email, LocalDate birthday, Integer age, String username, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.birthday = birthday;
        this.age = age;
        this.username = username;
        this.password = password;
        this.enabled = false;
    }
}
