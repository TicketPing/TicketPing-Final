package com.ticketPing.gateway.presentation.controller;

import com.ticketPing.gateway.common.exception.CircuitBreakerErrorCase;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import response.CommonResponse;

import java.util.concurrent.TimeoutException;

@Slf4j
@RestController
public class FallbackController {

    @RequestMapping(value = "/fallback/default", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
    public Mono<ResponseEntity<CommonResponse<Object>>> defaultFallback(ServerWebExchange exchange) {
        Throwable exception = exchange.getAttribute(ServerWebExchangeUtils.CIRCUITBREAKER_EXECUTION_EXCEPTION_ATTR);
        log.info("라우팅 예외: {}", exception != null ? exception.getMessage() : "Unknown error");

        return Mono.justOrEmpty(exception)
                .map(this::handleCircuitBreakerException)
                .defaultIfEmpty(createServiceUnavailableResponse(CircuitBreakerErrorCase.SERVICE_UNAVAILABLE));
    }

    private ResponseEntity<CommonResponse<Object>> handleCircuitBreakerException(Throwable ex) {
        if (ex instanceof TimeoutException) {
            return createServiceUnavailableResponse(CircuitBreakerErrorCase.SERVICE_UNAVAILABLE);
        } else if (ex instanceof CallNotPermittedException) {
            return createServiceUnavailableResponse(CircuitBreakerErrorCase.SERVICE_IS_OPEN);
        } else {
            return createServiceUnavailableResponse(CircuitBreakerErrorCase.SERVICE_UNAVAILABLE);
        }
    }

    private ResponseEntity<CommonResponse<Object>> createServiceUnavailableResponse(CircuitBreakerErrorCase errorCase) {
        return ResponseEntity.status(errorCase.getHttpStatus())
                .body(CommonResponse.error(errorCase));
    }
}
