package edu.samir.schooldemo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;

@Component
public class MyAuthenticationProvider implements AuthenticationProvider {

    @Autowired private UserDetailsManager userDetailsManager;
    @Autowired private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException { // here we implement the authentication logic

        // if the Authentication isn't supported by the AP then return null
        if ( !supports(authentication.getClass())){
            return null;
        }

        String username = authentication.getName();
        String password = String.valueOf(authentication.getCredentials());

        UserDetails userDetails = userDetailsManager.loadUserByUsername(username);

        // if the request is authenticated we should return an fully authenticated (with authorities) authentication instance
        if (userDetails != null){
            if (passwordEncoder.matches(password, userDetails.getPassword())){
                Authentication fullyAuthentication =
                        new UsernamePasswordAuthenticationToken(username, password, userDetails.getAuthorities());
                return fullyAuthentication;
            }
        }

        // if the request is not authenticated we should throw AuthenticationException
        throw new BadCredentialsException("Request could not be authenticated");
    }

    @Override
    public boolean supports(Class<?> authenticationType) {
        return UsernamePasswordAuthenticationToken.class.equals(authenticationType);
    }
}
