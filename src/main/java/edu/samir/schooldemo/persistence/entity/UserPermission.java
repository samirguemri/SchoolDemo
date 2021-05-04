package edu.samir.schooldemo.persistence.entity;

import edu.samir.schooldemo.persistence.entity.enums.Permission;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@FieldDefaults(level= AccessLevel.PRIVATE)
@NoArgsConstructor
@Data
@Entity
public class UserPermission {

    @Id
    @Column(updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    Permission permission;

    public UserPermission(Permission permission) {
        this.permission = permission;
    }
}
