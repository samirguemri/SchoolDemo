package edu.samir.schooldemo.persistence.entity;

import edu.samir.schooldemo.persistence.entity.enums.Role;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.Collection;

@FieldDefaults(level= AccessLevel.PRIVATE)
@NoArgsConstructor
@Data
@Entity
public class UserRole {

    @Id
    @Column(updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    Role role;

    @ManyToMany(cascade=CascadeType.ALL)
    @JoinTable(
            name = "role_permission_association",
            joinColumns = @JoinColumn(name = "roleId", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "permissionId", referencedColumnName = "id"),
            foreignKey = @ForeignKey(name = "ROLE_FOREIGN_KEY_CONSTRAINTS"),
            inverseForeignKey = @ForeignKey(name = "PERMISSION_FOREIGN_KEY_CONSTRAINTS")
    )
    @Column
    Collection<UserPermission> permissions;

    public UserRole(Role role) {
        this.role = role;
    }
}
