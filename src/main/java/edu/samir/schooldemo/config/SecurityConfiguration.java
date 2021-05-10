package edu.samir.schooldemo.config;

import edu.samir.schooldemo.security.provider.OtpAuthenticationProvider;
import edu.samir.schooldemo.security.provider.TokenAuthenticationProvider;
import edu.samir.schooldemo.security.provider.UsernamePasswordAuthProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableWebSecurity(debug = false)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired private AuthenticationProvider myAuthenticationProvider;
    @Autowired private UsernamePasswordAuthProvider usernamePasswordAuthProvider;
    @Autowired private OtpAuthenticationProvider otpAuthenticationProvider;
    @Autowired private TokenAuthenticationProvider tokenAuthenticationProvider;

    private static final String[] CSRF_IGNORE = {"/login"};

    // Authentication Configuration
    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(this.myAuthenticationProvider)
                .authenticationProvider(this.usernamePasswordAuthProvider)
                .authenticationProvider(this.otpAuthenticationProvider)
                .authenticationProvider(this.tokenAuthenticationProvider);
        return;
    }

    // Authorization Configuration
    @Override
    protected void configure(HttpSecurity http) throws Exception { // What and how to secure

        http.csrf().disable();
        http.httpBasic();
/*
        http.formLogin()
                .loginPage("/login").permitAll()
                    .defaultSuccessUrl("/userhomepage", true)
                .and()
                .rememberMe()
                .tokenValiditySeconds(((int) TimeUnit.DAYS.toSeconds(10)))
                .and()
                .logout()
                    .logoutUrl("/logout")
                    .logoutRequestMatcher(new AntPathRequestMatcher("logout", HttpMethod.GET.name()))
                    .invalidateHttpSession(true)
                    .clearAuthentication(true)
                    .deleteCookies("JSESSIONID", "remember-me")
                    .logoutSuccessUrl("/login");
*/

        http.authorizeRequests()
                .mvcMatchers("/api/manager/**").hasAnyRole("MANAGER")
                .mvcMatchers("/api/admin/**").hasAnyRole("ADMIN","MANAGER")
                .mvcMatchers("/api/teacher/**").hasAnyRole("TEACHER","ADMIN")
                .mvcMatchers("/api/student/**").hasAnyRole("STUDENT","ADMIN")
                .mvcMatchers("/users/**").hasAnyRole("MANAGER","ADMIN")
                .mvcMatchers("/","/home","/index*","/static/css/**","/static/js/**").permitAll()
                .mvcMatchers("/api/user/registration/**").anonymous()
                .anyRequest().authenticated();

//        http.addFilterAfter(csrfLoggerFilter(), CsrfFilter.class);
//        http.addFilterAt(multifactorAuthenticationFilter(), BasicAuthenticationFilter.class)
//                .addFilterAfter(tokenAuthenticationFilter(), BasicAuthenticationFilter.class);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        //return new BCryptPasswordEncoder(10);
        return NoOpPasswordEncoder.getInstance();
    }

/*
    @Bean
    public CsrfLoggerFilter csrfLoggerFilter(){
        return new CsrfLoggerFilter();
    };

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
