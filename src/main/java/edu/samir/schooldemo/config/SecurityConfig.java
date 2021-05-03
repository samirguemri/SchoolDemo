package edu.samir.schooldemo.config;

import edu.samir.schooldemo.security.filter.MultifactorAuthenticationFilter;
import edu.samir.schooldemo.security.filter.TokenAuthenticationFilter;
import edu.samir.schooldemo.security.provider.OtpAuthenticationProvider;
import edu.samir.schooldemo.security.provider.TokenAuthenticationProvider;
import edu.samir.schooldemo.security.provider.UsernamePasswordAuthenticationProvider;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;

//@EnableAsync
@EnableWebSecurity(debug = false)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired private UsernamePasswordAuthenticationProvider usernamePasswordAuthenticationProvider;
    @Autowired private OtpAuthenticationProvider otpAuthenticationProvider;
    @Autowired private TokenAuthenticationProvider tokenAuthenticationProvider;

    @Autowired private Environment springEnvironment;

    @Override
    protected void configure(HttpSecurity http) throws Exception { // What and how to secure

        // Which authentication methods are allowed (formLogin(), httpBasic()) and how they are configured
        http.httpBasic();//formLogin();//

//        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
/*
        // What URLs to protect (authenticated()) and which ones are allowed (permitAll())
        http
            .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/api/user/registration/**").permitAll()
                .mvcMatchers(HttpMethod.GET, "/home").permitAll()
                .antMatchers( "/h2-console/**").permitAll()
                .anyRequest().authenticated();//permitAll();//


        // protect application from malicious request
        http.csrf( customizer -> {
            // disable csrf for any request that starts with "/csrfdisabled/"
            customizer.ignoringAntMatchers("/csrfdisabled/**");
        });
*/

        http.csrf().disable();

        // disable client to access response if not allowed
        http.cors( customizer -> {
            CorsConfigurationSource configurationSource = request -> {
                CorsConfiguration corsConfiguration = new CorsConfiguration();

                // We can use Environment.property
                String allowedOrigin = this.springEnvironment.getProperty("app.cors.allowed-origin");
                corsConfiguration.setAllowedOrigins(List.of(allowedOrigin));
                corsConfiguration.setAllowedMethods(List.of("GET","POST"));

                return corsConfiguration;
            };
            customizer.configurationSource(configurationSource);
        });

        http.addFilterAt(multifactorAuthenticationFilter(), BasicAuthenticationFilter.class)
                .addFilterAfter(tokenAuthenticationFilter(), BasicAuthenticationFilter.class);

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(this.usernamePasswordAuthenticationProvider)
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

    @Bean
    public MultifactorAuthenticationFilter multifactorAuthenticationFilter(){
        return new MultifactorAuthenticationFilter();
    }

    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter(){
        return new TokenAuthenticationFilter();
    }

/*
    @Bean
    public InitializingBean initializingBean(){
        return () -> {
            SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
        };
    }
*/
}
