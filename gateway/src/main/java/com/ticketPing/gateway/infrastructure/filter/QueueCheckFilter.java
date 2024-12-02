package com.ticketPing.gateway.infrastructure.filter;

import static com.ticketPing.gateway.common.exception.FilterErrorCase.PERFORMANCE_ID_NOT_FOUND;
import static com.ticketPing.gateway.common.exception.FilterErrorCase.USER_ID_NOT_FOUND;

import com.ticketPing.gateway.application.service.QueueCheckService;
import exception.ApplicationException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class QueueCheckFilter {

    private static final String PERFORMANCE_ID_PARAM = "performanceId";

    private final QueueCheckService queueCheckService;

    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return Mono.zip(getPerformanceIdFromQueryParams(exchange), getUserIdFromAuthentication())
                .doOnSuccess(tuple -> log.info("공연 ID: {}, 유저 ID: {}", tuple.getT1(), tuple.getT2()))
                .flatMap(tuple -> handleApiRequest(exchange, chain, tuple.getT1(), tuple.getT2()));
    }

    private Mono<String> getUserIdFromAuthentication() {
        return ReactiveSecurityContextHolder.getContext()
                .map(context -> context.getAuthentication().getName())
                .switchIfEmpty(Mono.error(new ApplicationException(USER_ID_NOT_FOUND)));
    }

    private Mono<String> getPerformanceIdFromQueryParams(ServerWebExchange exchange) {
        return Optional.ofNullable(exchange.getRequest().getQueryParams().getFirst(PERFORMANCE_ID_PARAM))
                .map(Mono::just)
                .orElseGet(() -> Mono.error(new ApplicationException(PERFORMANCE_ID_NOT_FOUND)));
    }

    private Mono<Void> handleApiRequest(ServerWebExchange exchange, GatewayFilterChain chain, String performanceId, String userId) {
        String path = exchange.getRequest().getURI().getPath();
        String method = exchange.getRequest().getMethod().name();

        return APIType.findByRequest(path, method)
                .doOnSuccess(api -> log.info("API 타입: {}", api))
                .flatMap(api ->
                        switch (api) {
                            case ENTER_WAITING_QUEUE -> handleEnterWaitingQueueAPI(exchange, chain, performanceId);
                            case GET_QUEUE_INFO -> handleGetQueueTokenAPI(exchange, chain, performanceId);
                            case CREATE_ORDER, REQUEST_PAYMENT -> handleReservationAPI(exchange, chain, userId, performanceId);
                        })
                .switchIfEmpty(chain.filter(exchange));
    }

    private Mono<Void> handleEnterWaitingQueueAPI(ServerWebExchange exchange, GatewayFilterChain chain, String performanceId) {
        return queueCheckService.checkIsPerformanceSoldOut(performanceId)
                .then(queueCheckService.checkHasTooManyWaitingUsers(performanceId))
                .then(chain.filter(exchange));
    }

    private Mono<Void> handleGetQueueTokenAPI(ServerWebExchange exchange, GatewayFilterChain chain, String performanceId) {
        return queueCheckService.checkIsPerformanceSoldOut(performanceId)
                .then(chain.filter(exchange));
    }

    private Mono<Void> handleReservationAPI(ServerWebExchange exchange, GatewayFilterChain chain, String userId, String performanceId) {
        return queueCheckService.checkIsPerformanceSoldOut(performanceId)
                .then(queueCheckService.checkIsUserAvailable(userId, performanceId))
                .then(chain.filter(exchange));
    }

}