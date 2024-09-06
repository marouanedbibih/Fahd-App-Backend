package org.fahdpln.backend.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtFilter jwtFilter;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService, JwtFilter jwtFilter) {
        this.customUserDetailsService = customUserDetailsService;
        this.jwtFilter = jwtFilter;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Disable CSRF
        http.csrf(AbstractHttpConfigurer::disable);
        // Authorize requests based on the URL and the role of the user
        http.authorizeHttpRequests(request -> {
            request
                    // Public endpoints
                    .requestMatchers("/api/v1/login").permitAll()
                    .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                    // // Department endpoints
                    .requestMatchers(HttpMethod.GET,"/api/v1/departements").hasAuthority("ADMIN")
                    .requestMatchers(HttpMethod.GET,"/api/v1/departements/search").hasAuthority("ADMIN")
                    .requestMatchers(HttpMethod.GET,"/api/v1/departement/**").hasAuthority("ADMIN")
                    .requestMatchers(HttpMethod.POST,"/api/v1/departement").hasAuthority("ADMIN")
                    .requestMatchers(HttpMethod.PUT,"/api/v1/departement/**").hasAuthority("ADMIN")
                    .requestMatchers(HttpMethod.DELETE,"/api/v1/departement/**").hasAuthority("ADMIN")
                    .requestMatchers("/api/v1/departements/dropdown").hasAnyAuthority("ADMIN","SECRETARY")
                    // // Employee endpoints
                    .requestMatchers(HttpMethod.GET,"/api/v1/employees/**").hasAnyAuthority("ADMIN","SECRETARY")
                    .requestMatchers(HttpMethod.GET,"/api/v1/employees/search").hasAnyAuthority("ADMIN","SECRETARY")
                    .requestMatchers(HttpMethod.POST,"/api/v1/employee").hasAnyAuthority("ADMIN","SECRETARY")
                    .requestMatchers(HttpMethod.PUT,"/api/v1/employee/**").hasAnyAuthority("ADMIN","SECRETARY")
                    .requestMatchers(HttpMethod.DELETE,"/api/v1/employee/**").hasAnyAuthority("ADMIN","SECRETARY")
                    // Schedule endpoints
                    .requestMatchers(HttpMethod.GET, "/api/v1/employee/{employeeId}/schedules").hasAnyAuthority("ADMIN", "SECRETARY")
                    .requestMatchers(HttpMethod.POST, "/api/v1/employee/{employeeId}/schedules").hasAnyAuthority("ADMIN", "SECRETARY")
                    .requestMatchers(HttpMethod.DELETE, "/api/v1/employee/{employeeId}/schedules").hasAnyAuthority("ADMIN", "SECRETARY")
                    .anyRequest().authenticated();
        });
        // Set the session management to stateless
        http.sessionManagement(
                sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // Set the authentication provider
        http.authenticationProvider(authenticationProvider());

        http.exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler));

        // Add the JWT filter
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        // http.httpBasic(Customizer.withDefaults());
        return (SecurityFilterChain) http.build();
    }

    // Password encoder bean
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Authentication provider bean
    @Bean
    public AuthenticationProvider authenticationProvider() {
        // Define the DaoAuthenticationProvider
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

        // Set the custom user details service
        provider.setUserDetailsService(customUserDetailsService);
        // Set the password encoder
        provider.setPasswordEncoder(passwordEncoder());

        // Return the provider
        return provider;
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
