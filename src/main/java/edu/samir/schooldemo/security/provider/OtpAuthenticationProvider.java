package edu.samir.schooldemo.security.provider;

import edu.samir.schooldemo.persistence.repository.OptRepository;
import edu.samir.schooldemo.security.authentication.OtpAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class OtpAuthenticationProvider implements AuthenticationProvider {

    private final OptRepository optRepository;
    private final UserDetailsManager userDetailsManager;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public OtpAuthenticationProvider(OptRepository optRepository, UserDetailsManager userDetailsManager, PasswordEncoder passwordEncoder) {
        this.optRepository = optRepository;
        this.userDetailsManager = userDetailsManager;
        this.passwordEncoder = passwordEncoder;
    }
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String username = authentication.getName();
        String otpToken = String.valueOf(authentication.getCredentials());

        UserDetails userDetails = this.userDetailsManager.loadUserByUsername(username);
        var otp = this.optRepository.findOptByUsername(username);

        if (otp.isPresent() && otpToken.equals(otp.get().getOtp())){
            boolean validOtp = checkOtpValidity(otp.get().getValidity());
            if (validOtp) {
                return new OtpAuthentication(username, otpToken, userDetails.getAuthorities());
            }
        }

        throw  new BadCredentialsException("OtpAuthentication could not be authenticated");
    }

    private boolean checkOtpValidity(LocalDateTime validity) {
        return validity.compareTo(LocalDateTime.now()) >= 0;
    }

    @Override
    public boolean supports(Class<?> authenticationClass) {
        return authenticationClass.equals(OtpAuthentication.class);
    }
}
