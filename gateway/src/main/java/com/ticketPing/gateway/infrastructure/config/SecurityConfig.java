package com.ticketPing.gateway.infrastructure.config;

import com.ticketPing.gateway.infrastructure.filter.JwtFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableWebFluxSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .authorizeExchange(exchange -> exchange
                        .pathMatchers(HttpMethod.OPTIONS).permitAll()
                        .pathMatchers("/api/v1/auth/login").permitAll()
                        .pathMatchers("/api/v1/auth/validate").permitAll()
                        .pathMatchers("/api/v1/auth/refresh").permitAll()
                        .pathMatchers("/api/v1/users/signup").permitAll()
                        .pathMatchers(HttpMethod.GET, "/api/v1/performances").permitAll()
                        .pathMatchers(HttpMethod.GET, "/api/v1/performances/{performanceId}").permitAll()
                        .pathMatchers("/actuator/prometheus").permitAll()
                        .pathMatchers("/swagger-ui/**", "/v3/api-docs/**", "/webjars/**").permitAll()
                        .anyExchange().authenticated()
                )
                .securityContextRepository(jwtFilter)
                .build();
    }

}