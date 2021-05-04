package edu.samir.schooldemo.config;

import edu.samir.schooldemo.security.provider.OtpAuthenticationProvider;
import edu.samir.schooldemo.security.provider.TokenAuthenticationProvider;
import edu.samir.schooldemo.security.provider.UsernamePasswordAuthProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity(debug = false)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired private AuthenticationProvider myAuthenticationProvider;
    @Autowired private UsernamePasswordAuthProvider usernamePasswordAuthProvider;
    @Autowired private OtpAuthenticationProvider otpAuthenticationProvider;
    @Autowired private TokenAuthenticationProvider tokenAuthenticationProvider;

    @Autowired private Environment springEnvironment;

    @Override
    protected void configure(HttpSecurity http) throws Exception { // What and how to secure

        http.formLogin()
            .and().csrf().disable();

        http
            .authorizeRequests()
                .antMatchers("/api/user/registration/**").permitAll()
            .anyRequest().authenticated();//permitAll();//

//        http.addFilterAt(multifactorAuthenticationFilter(), BasicAuthenticationFilter.class)
//                .addFilterAfter(tokenAuthenticationFilter(), BasicAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(this.myAuthenticationProvider)
                .authenticationProvider(this.usernamePasswordAuthProvider)
                .authenticationProvider(this.otpAuthenticationProvider)
                .authenticationProvider(this.tokenAuthenticationProvider);
        return;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
        //return NoOpPasswordEncoder.getInstance();
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

/*
    @Bean
    public MultifactorAuthenticationFilter multifactorAuthenticationFilter(){
        return new MultifactorAuthenticationFilter();
    }

    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter(){
        return new TokenAuthenticationFilter();
    }

    @Bean
    public InitializingBean initializingBean(){
        return () -> {
            SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
        };
    }
*/
}
