package com.anish.seatbooking.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SpringConfig {
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .csrf(csrf -> csrf.disable())
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())
                .sessionManagement(session -> session.sessionCreationPolicy
                        (SessionCreationPolicy.STATELESS)
                ).exceptionHandling(exception
                        -> exception.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .authorizeHttpRequests(auth -> auth
                                .requestMatchers(HttpMethod.POST, "/api/auth/admin").hasRole("ADMIN")
//                        registration and login
                        .requestMatchers("/api/auth/**").permitAll()
//                        only admin can create seats
                        .requestMatchers(HttpMethod.POST, "/api/seat/**").hasRole("ADMIN")
//                        both can view seats
                       .requestMatchers(HttpMethod.GET, "/api/seat","/api/seat/**").hasAnyRole("ADMIN", "USER")
//                      both users and admins can book and cancel seats
                        .requestMatchers(HttpMethod.PUT, "/api/seats/**").hasAnyRole("ADMIN", "USER")
//                        to allow the bookings
                        .requestMatchers(HttpMethod.POST, "/api/bookings/**").hasAnyRole("ADMIN", "USER")
//                        to delete the bookings
                        .requestMatchers(HttpMethod.DELETE, "/api/bookings/**").hasAnyRole("ADMIN", "USER")
//                                to allow admin to set seats
                                .requestMatchers(HttpMethod.GET, "/api/admin/**").hasRole("ADMIN")
//                      all other request that require authentication
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
