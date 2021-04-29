package edu.samir.schooldemo.security.authentication;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class OtpAuthentication extends UsernamePasswordAuthenticationToken {

    public OtpAuthentication(String email, String  otp) {
        super(email, otp);
    }

    public OtpAuthentication(String email, String  otp, Collection<? extends GrantedAuthority> authorities) {
        super(email, otp, authorities);
    }
}
