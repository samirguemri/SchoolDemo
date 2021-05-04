package edu.samir.schooldemo.security.userdetail;

import edu.samir.schooldemo.persistence.entity.UserRole;
import edu.samir.schooldemo.persistence.entity.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
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
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<UserRole> userRoles = this.getUserEntity().getUserRoles();
        return userRoles.stream()
                .map(userRole -> new SimpleGrantedAuthority(userRole.getRole().getRoleName()) )
                .collect(Collectors.toList());
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
