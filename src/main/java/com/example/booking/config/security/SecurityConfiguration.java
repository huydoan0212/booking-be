package com.example.booking.config.security;


import com.example.booking.common.constant.ExceptionMessage;
import com.example.booking.config.security.jwt.JwtAuthenticationFilter;
import com.example.booking.interceptor.Violation;
import com.example.booking.interceptor.response.GlobalResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.example.booking.common.enums.Role.ADMIN;


@Slf4j
@Configuration
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authenticationProvider;
    private final ObjectMapper objectMapper;

    public SecurityConfiguration(AuthenticationProvider authenticationProvider, JwtAuthenticationFilter jwtAuthenticationFilter, ObjectMapper objectMapper) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.authenticationProvider = authenticationProvider;
        this.objectMapper = objectMapper;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable).authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers(HttpMethod.OPTIONS).permitAll()
                        // PUBLIC APIs - Accessible to everyone
                        .requestMatchers("/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-resources/**",
                                "/webjars/**",
                                "/ws-ticket/**",
                                "/app/**").permitAll()
                        .requestMatchers(HttpMethod.POST,
                                "/auth/login",
                                "/user/forgot-password",
                                "/user/check-otp",
                                "/user/reset-password",
                                "/user/register",
                                "/user/active-profile").permitAll()
                        .requestMatchers(HttpMethod.GET,
                                "/movie/**",
                                "/category/**",
                                "/cinema/**",
                                "/cinema-hall/**",
                                "/seat/**",
                                "/show-time/**",
                                "/booking/**",
                                "/discount/**",
                                "/ticket/**")
                        .permitAll()
                        // AUTHENTICATED APIs - Requires login
                        .requestMatchers(HttpMethod.GET, "/user/profile").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/user/profile").authenticated()

                        // ADMIN APIs - Requires ADMIN role
                        .requestMatchers(HttpMethod.GET,
                                "/user/{id}",
                                "/user/",
                                "/user/search").hasRole(ADMIN.toString())
                        .requestMatchers(HttpMethod.POST,
                                "/user/{role}",
                                "/movie/**",
                                "/category/**",
                                "/cinema/**",
                                "/cinema-hall/**",
                                "/seat/**",
                                "/show-time/**",
                                "/booking/**",
                                "/discount/**",
                                "/ticket/**").hasRole(ADMIN.toString())
                        .requestMatchers(HttpMethod.PUT,
                                "/user/{id}",
                                "/movie/**",
                                "/category/**",
                                "/cinema/**",
                                "/cinema-hall/**",
                                "/seat/**",
                                "/show-time/**",
                                "/booking/**",
                                "/discount/**",
                                "/ticket/**").hasRole(ADMIN.toString())
                        .requestMatchers(HttpMethod.DELETE,
                                "/user/{id}",
                                "/movie/**",
                                "/category/**",
                                "/cinema/**",
                                "/cinema-hall/**",
                                "/seat/**",
                                "/show-time/**",
                                "/booking/**",
                                "/discount/**",
                                "/ticket/**").hasRole(ADMIN.toString()))

                .exceptionHandling(exceptionHandlingConfigurer -> exceptionHandlingConfigurer
                        .accessDeniedHandler((request, response, e) -> {
                            GlobalResponse globalResponse = new GlobalResponse(
                                    ExceptionMessage.FORBIDDEN,
                                    HttpStatus.FORBIDDEN.value(),
                                    new Violation(null, "You don't have permission to access this resource"),
                                    request.getRequestURI()
                            );
                            response.setContentType("application/json;charset=UTF-8");
                            response.setStatus(HttpStatus.FORBIDDEN.value());
                            response.getWriter().write(new ObjectMapper().writeValueAsString(globalResponse));
                        })

                        .authenticationEntryPoint((request, response, e) -> {
                            GlobalResponse globalResponse = new GlobalResponse(
                                    ExceptionMessage.UNAUTHORIZED,
                                    HttpStatus.UNAUTHORIZED.value(),
                                    new Violation(null, "Access denied"),
                                    request.getRequestURI()
                            );
                            response.setContentType("application/json;charset=UTF-8");
                            response.setStatus(HttpStatus.UNAUTHORIZED.value());
                            response.getWriter().write(objectMapper.writeValueAsString(globalResponse));
                        })
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }


}
