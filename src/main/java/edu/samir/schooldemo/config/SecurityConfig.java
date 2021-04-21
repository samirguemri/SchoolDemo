package edu.samir.schooldemo.config;

import edu.samir.schooldemo.security.MyAuthenticationProvider;
import edu.samir.schooldemo.security.MyJpaUserDetailsManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired private DataSource dataSource;
    @Autowired private AuthenticationProvider authenticationProvider;

    @Override
    protected void configure(HttpSecurity http) throws Exception { // What and how to secure

        // Which authentication methods are allowed (formLogin(), httpBasic()) and how they are configured
        http
            .formLogin()//httpBasic()//
                //.loginPage("login")
                    .permitAll()
                //.failureForwardUrl("home")
        .and()
            .logout()
                .permitAll();
        //.httpBasic();

        // What URLs to protect (authenticated()) and which ones are allowed (permitAll())
        http
            .authorizeRequests()
                .mvcMatchers("*")
                    .permitAll()
                .anyRequest()
                    .authenticated();//permitAll();//
    }

    @Bean
    public UserDetailsManager userDetailsManager() { // Where to get users from
        //return new JdbcUserDetailsManager(dataSource());
        return new MyJpaUserDetailsManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        //return new BCryptPasswordEncoder();
        return NoOpPasswordEncoder.getInstance();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider);
    }

    private DataSource dataSource(){
        return dataSource;
    }
}
