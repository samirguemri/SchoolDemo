package edu.samir.schooldemo.security;

import edu.samir.schooldemo.entities.Permission;
import edu.samir.schooldemo.entities.Student;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class SecurityStudent implements UserDetails {

    private final Student student;

    public SecurityStudent(Student student) {
        this.student = student;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of( () -> String.valueOf(Permission.STUDENT_READ));
    }

    @Override
    public String getPassword() {
        return student.getPassword();
    }

    @Override
    public String getUsername() {
        return student.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
