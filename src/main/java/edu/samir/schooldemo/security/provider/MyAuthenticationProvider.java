package edu.samir.schooldemo.security.provider;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class MyAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsManager userDetailsManager;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException { // here we implement the authentication logic

        // if the Authentication isn't supported by the AP then return null
        // the test is already done in the ProviderManager.authenticate()

        String username = authentication.getName();
        String password = String.valueOf(authentication.getCredentials());

        UserDetails userDetails = userDetailsManager.loadUserByUsername(username);

        // if the request is authenticated we should return an fully authenticated (with authorities) authentication instance
        if (userDetails != null){
            if (passwordEncoder.matches(password, userDetails.getPassword())){
                if (!userDetails.isEnabled()){
                    throw new BadCredentialsException("User not yet enabled");
                }
                Authentication fullyAuthentication =
                        new UsernamePasswordAuthenticationToken(username, password, userDetails.getAuthorities());
                return fullyAuthentication;
            }
        }

        // if the request is not authenticated we should throw AuthenticationException
        throw new BadCredentialsException("Request could not be authenticated");
    }

    @Override
    public boolean supports(Class<?> authenticationClass) {
        return authenticationClass.equals(UsernamePasswordAuthenticationToken.class);
    }
}
