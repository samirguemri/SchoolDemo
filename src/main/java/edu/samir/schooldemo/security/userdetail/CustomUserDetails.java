package edu.samir.schooldemo.security.userdetail;

import edu.samir.schooldemo.persistence.entity.UserRole;
import edu.samir.schooldemo.persistence.entity.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final UserEntity userEntity;

    public UserEntity getUserEntity() {
        return userEntity;
    }

    @Override
    public String getUsername() {
        return userEntity.getUsername();
    }

    @Override
    public String getPassword() {
        return userEntity.getPassword();
    }

    @Override
    @Transactional
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new HashSet<>();
        Collection<UserRole> userRoles = userEntity.getUserRoles();
        for (UserRole userRole : userRoles) {
            authorities.add(new SimpleGrantedAuthority(userRole.getRole().name()));
            authorities.addAll(userRole.getPermissions()
                    .stream()
                    .map(userPermission -> new SimpleGrantedAuthority(userPermission.getPermission().name()))
                    .collect(Collectors.toSet())
            );
        }
        return authorities;
    }

    @Override
    public boolean isEnabled() {
        return userEntity.isEnabled();
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
}
