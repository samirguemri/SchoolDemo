package edu.samir.schooldemo.security.filter;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CsrfLoggerFilter extends OncePerRequestFilter {

    public static final String CSRF_NAME = "X-CSRF-TOKEN";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());

        if (csrfToken != null) {
            response.setHeader(CSRF_NAME, csrfToken.getToken());
        }

        filterChain.doFilter(request, response);
    }
}
