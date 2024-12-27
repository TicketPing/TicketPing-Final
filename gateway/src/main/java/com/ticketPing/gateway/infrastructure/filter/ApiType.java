package com.ticketPing.gateway.infrastructure.filter;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Getter
@RequiredArgsConstructor
public enum ApiType {

    ENTER_WAITING_QUEUE("/api/v1/waiting-queue", HttpMethod.POST),
    GET_QUEUE_INFO("/api/v1/waiting-queue", HttpMethod.GET),
    CREATE_ORDER("/api/v1/orders", HttpMethod.POST);

    private final String path;
    private final HttpMethod method;

    public static Mono<ApiType> findByRequest(String requestPath, String httpMethod) {
        return Flux.fromArray(ApiType.values())
                .filter(api -> api.matches(requestPath, httpMethod))
                .next()
                .switchIfEmpty(Mono.empty());
    }

    private boolean matches(String requestPath, String httpMethod) {
        return requestPath.equals(this.path) && this.method.name().equals(httpMethod);
    }

}