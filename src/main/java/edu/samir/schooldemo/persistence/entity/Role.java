package edu.samir.schooldemo.persistence.entity;

import edu.samir.schooldemo.persistence.entity.enums.RoleEnum;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

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

/*
    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(
            name = "role_permission_association",
            joinColumns = @JoinColumn(name = "roleId", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "permissionId", referencedColumnName = "id"),
            foreignKey = @ForeignKey(name = "ROLE_FOREIGN_KEY_CONSTRAINTS"),
            inverseForeignKey = @ForeignKey(name = "PERMISSION_FOREIGN_KEY_CONSTRAINTS")
    )
    @Column
    Set<Permission> permissions;
*/

    public Role(RoleEnum role) {
        this.role = role;
    }
}
