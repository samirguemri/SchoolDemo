package edu.samir.schooldemo.security.provider;

import edu.samir.schooldemo.security.authentication.TokenAuthentication;
import edu.samir.schooldemo.security.manager.TokenManager;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;

import java.util.Collections;

@AllArgsConstructor
@Component
public class TokenAuthenticationProvider implements AuthenticationProvider {

    private final TokenManager tokenManager;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String token = authentication.getName();

        if ( this.tokenManager.contains(token) ) {
            return new TokenAuthentication(token, null, Collections.emptyList());
        } else {
            throw new BadCredentialsException("User with the given username does not exists OR bad token");
        }
    }

    @Override
    public boolean supports(Class<?> authenticationClass) {
        return authenticationClass.equals(TokenAuthentication.class);
    }
}
