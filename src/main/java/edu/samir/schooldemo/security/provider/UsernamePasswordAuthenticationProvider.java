package edu.samir.schooldemo.security.provider;

import edu.samir.schooldemo.security.authentication.UsernamePasswordAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;

@Component
@Primary
public class UsernamePasswordAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsManager userDetailsManager;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsernamePasswordAuthenticationProvider(UserDetailsManager userDetailsManager, PasswordEncoder passwordEncoder) {
        this.userDetailsManager = userDetailsManager;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String username = authentication.getName();
        String password = String.valueOf(authentication.getCredentials());

        UserDetails userDetails = userDetailsManager.loadUserByUsername(username);

        // if the request is authenticated we should return an fully authenticated (with authorities) authentication instance
        if (userDetails != null){
            if (passwordEncoder.matches(password, userDetails.getPassword())){
                //final Authentication result = super.authenticate(authentication);
                return new UsernamePasswordAuthentication(username, password, userDetails.getAuthorities());
            }
        }

        // if the request is not authenticated we should throw AuthenticationException
        throw new BadCredentialsException("UsernamePasswordAuthentication could not be authenticated");
    }

    @Override
    public boolean supports(Class<?> authenticationClass) {
        return authenticationClass.equals(UsernamePasswordAuthentication.class);
    }
}
