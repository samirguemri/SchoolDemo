package edu.samir.schooldemo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class MyAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private AuthenticationManager authenticationManager;
    private BasicAuthenticationConverter authenticationConverter = new BasicAuthenticationConverter();

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        // Convert and extract the Authentication Token
        UsernamePasswordAuthenticationToken authenticationToken = authenticationConverter.convert(request);

        // Authenticate the httpRequest
        try {
            Authentication authenticateResult = authenticationManager.authenticate(authenticationToken);

            if (authenticateResult.isAuthenticated()){ // if the token is authenticated

                // Add the authenticateResult to the Spring Security Context
                SecurityContextHolder.getContext().setAuthentication(authenticateResult);
                filterChain.doFilter(request, response);
            } else {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            }
        }catch (AuthenticationException exception){
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }
    }
}
