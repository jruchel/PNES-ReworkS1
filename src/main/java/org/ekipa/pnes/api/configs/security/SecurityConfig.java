package org.ekipa.pnes.api.configs.security;

import org.ekipa.pnes.api.configs.security.fillters.JwtFillter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends MySecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private JwtFillter jwtFillter;

    static {
        setWhitelist(Arrays.asList("/swagger-resources/**",
                "/v2/api-docs",
                "/swagger-ui.html",
                "/webjars/**"));
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        setupEndpoints(http).authorizeRequests()
                .anyRequest().authenticated().and()
                .csrf().disable().cors().and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtFillter, UsernamePasswordAuthenticationFilter.class);
    }

}