package edu.samir.schooldemo.persistence.entity;

import edu.samir.schooldemo.persistence.entity.enums.PermissionEnum;
import edu.samir.schooldemo.persistence.entity.enums.RoleEnum;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@FieldDefaults(level= AccessLevel.PRIVATE)
@NoArgsConstructor
@Data
@Entity
public class Permission {

    @Id
    @Column(updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    PermissionEnum permission;

    public Permission(PermissionEnum permission) {
        this.permission = permission;
    }
}
