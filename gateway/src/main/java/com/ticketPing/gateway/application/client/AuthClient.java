package com.ticketPing.gateway.application.client;

import auth.UserCacheDto;
import reactor.core.publisher.Mono;

public interface AuthClient {
    public Mono<UserCacheDto> validateToken(String token);
}
