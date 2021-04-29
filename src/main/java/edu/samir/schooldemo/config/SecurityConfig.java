package edu.samir.schooldemo.config;

import edu.samir.schooldemo.security.JpaUserDetailsManager;
import edu.samir.schooldemo.security.filter.TokenAuthenticationFilter;
import edu.samir.schooldemo.security.filter.MultifactorAuthenticationFilter;
import edu.samir.schooldemo.security.provider.OtpAuthenticationProvider;
import edu.samir.schooldemo.security.provider.TokenAuthenticationProvider;
import edu.samir.schooldemo.security.provider.UsernamePasswordAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity(debug = false)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UsernamePasswordAuthenticationProvider usernamePasswordAuthenticationProvider;

    @Autowired
    private OtpAuthenticationProvider otpAuthenticationProvider;

    @Autowired
    private TokenAuthenticationProvider tokenAuthenticationProvider;

    @Autowired
    private DataSource dataSource;


    @Override
    protected void configure(HttpSecurity http) throws Exception { // What and how to secure

        http.addFilterAt(multifactorAuthenticationFilter(), BasicAuthenticationFilter.class)
                .addFilterAfter(tokenAuthenticationFilter(), BasicAuthenticationFilter.class);

        // Which authentication methods are allowed (formLogin(), httpBasic()) and how they are configured
        http
            .httpBasic().and().csrf().disable();//formLogin(withDefaults());//

        // What URLs to protect (authenticated()) and which ones are allowed (permitAll())
        http
            .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/api/user/registration/**").permitAll()
                .mvcMatchers(HttpMethod.GET, "/home").permitAll()
                .anyRequest().authenticated();//permitAll();//
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(usernamePasswordAuthenticationProvider)
                .authenticationProvider(otpAuthenticationProvider)
                    .authenticationProvider(tokenAuthenticationProvider);
        return;
    }

    @Bean
    public UserDetailsManager userDetailsManager() { // Where to get users from
        //return new JdbcUserDetailsManager(dataSource());
        return new JpaUserDetailsManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        //return new BCryptPasswordEncoder();
        return NoOpPasswordEncoder.getInstance();
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public MultifactorAuthenticationFilter multifactorAuthenticationFilter(){
        return new MultifactorAuthenticationFilter();
    }

    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter(){
        return new TokenAuthenticationFilter();
    }


    private DataSource dataSource(){
        return dataSource;
    }
}
