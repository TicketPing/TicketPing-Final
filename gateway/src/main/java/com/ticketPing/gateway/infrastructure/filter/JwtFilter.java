package com.ticketPing.gateway.infrastructure.filter;

import com.ticketPing.gateway.application.client.AuthClient;
import exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtFilter implements ServerSecurityContextRepository {

    private final AuthClient authClient;

    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        return null;
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange) {
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null) {
            return Mono.empty();
        }

        return authClient.validateToken(authHeader)
                .flatMap(response -> {
                    List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(response.role()));
                    Authentication authentication = new UsernamePasswordAuthenticationToken(
                            response.userId(), null, authorities
                    );

                    exchange.mutate()
                            .request(r -> r.headers(headers -> {
                                headers.add("X_USER_ID", String.valueOf(response.userId()));
                                headers.add("X_USER_ROLE", response.role());
                            }))
                            .build();

                    return Mono.just((SecurityContext) new SecurityContextImpl(authentication));
                })
                .onErrorResume(ApplicationException.class, e -> {
                    exchange.getResponse().setStatusCode(HttpStatus.SERVICE_UNAVAILABLE);
                    DataBuffer buffer = exchange.getResponse()
                            .bufferFactory()
                            .wrap(e.getMessage().getBytes(StandardCharsets.UTF_8));
                    return exchange.getResponse().writeWith(Mono.just(buffer))
                            .then(Mono.empty());
                })
                .onErrorResume(WebClientResponseException.Unauthorized.class, e -> Mono.empty());
    }

}