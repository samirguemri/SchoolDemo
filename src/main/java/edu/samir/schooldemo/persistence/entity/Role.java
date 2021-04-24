package edu.samir.schooldemo.persistence.entity;

import edu.samir.schooldemo.persistence.entity.enums.RoleEnum;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.List;

@FieldDefaults(level= AccessLevel.PRIVATE)
@NoArgsConstructor
@Data
@Entity
public class Role {

    @Id
    @Column(updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    RoleEnum role;

    @ManyToMany(mappedBy = "roles")
    List<User> users;

}
