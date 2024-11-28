package com.ticketPing.gateway.infrastructure.filter;

import com.ticketPing.gateway.application.client.AuthClient;
import com.ticketPing.gateway.infrastructure.client.AuthWebClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

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

        if (authHeader != null) {
            return authClient.validateToken(authHeader)
                    .flatMap(response -> {
                        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(response.role()));
                        Authentication authentication = new UsernamePasswordAuthenticationToken(
                                response.userId(), null, authorities
                        );

                        exchange.mutate()
                                .request(r -> r.headers(headers -> {
                                    headers.add("X_User_Id", String.valueOf(response.userId()));
                                    headers.add("X_User_Role", response.role());
                                }))
                                .build();

                        return Mono.just(new SecurityContextImpl(authentication));
                    });
        }

        return Mono.empty();
    }

}
