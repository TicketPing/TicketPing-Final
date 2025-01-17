package com.ticketPing.gateway.infrastructure.client;

import auth.UserCacheDto;
import com.ticketPing.gateway.application.client.AuthClient;
import com.ticketPing.gateway.common.exception.CircuitBreakerErrorCase;
import exception.ApplicationException;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.netty.channel.ChannelOption;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import response.CommonResponse;

import java.time.Duration;

@Component
public class AuthWebClient implements AuthClient {

    private final WebClient webClient;

    public AuthWebClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .clientConnector(new ReactorClientHttpConnector(
                        HttpClient.create()
                                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 1000)
                                .responseTimeout(Duration.ofSeconds(15))))
                .build();
    }

    @Retry(name = "authServiceRetry")
    @CircuitBreaker(name = "authServiceCircuitBreaker", fallbackMethod = "validateTokenFallback")
    public Mono<UserCacheDto> validateToken(String token) {
        return webClient.post()
                .uri("http://auth/api/v1/auth/validate")
                .header("Authorization", token)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<CommonResponse<UserCacheDto>>() {})
                .flatMap(response -> Mono.just(response.getData()));
    }

    private Mono<UserCacheDto> validateTokenFallback(String token, Throwable ex) {
        if (ex instanceof CallNotPermittedException) {
            return Mono.error(new ApplicationException(CircuitBreakerErrorCase.SERVICE_IS_OPEN));
        } else if (
                ex instanceof WebClientResponseException.BadGateway ||
                ex instanceof WebClientResponseException.GatewayTimeout ||
                ex instanceof WebClientResponseException.TooManyRequests ||
                ex instanceof WebClientResponseException.ServiceUnavailable
        ) {
            return Mono.error(new ApplicationException(CircuitBreakerErrorCase.SERVICE_UNAVAILABLE));
        } else if (ex instanceof WebClientResponseException) {
            return Mono.error(ex);
        } else {
            return Mono.error(new ApplicationException(CircuitBreakerErrorCase.SERVICE_UNAVAILABLE));
        }
    }

}
