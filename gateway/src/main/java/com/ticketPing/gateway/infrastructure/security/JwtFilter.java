package com.ticketPing.gateway.infrastructure.security;

import auth.UserCacheDto;
import com.ticketPing.gateway.infrastructure.client.AuthClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtFilter implements ServerSecurityContextRepository {

    @Lazy
    private final AuthClient authClient;

    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        return null;
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange) {
            String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

            if (authHeader != null) {
                UserCacheDto userCache = authClient.validateToken(authHeader).getBody().getData();

                Collection<GrantedAuthority> roleCollection = List.of(userCache::role);
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userCache.userId(), null, roleCollection);

                exchange.mutate()
                        .request(r -> r.headers(headers -> {
                            headers.add("X_User_Id", String.valueOf(userCache.userId()));
                            headers.add("X_User_Role", userCache.role());
                        }))
                        .build();

                return Mono.just(new SecurityContextImpl(authentication));

            }

            return Mono.empty();
    }

}
