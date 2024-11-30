package com.ticketPing.gateway.infrastructure.filter;

import static com.ticketPing.gateway.common.exception.FilterErrorCase.PERFORMANCE_SOLD_OUT;
import static com.ticketPing.gateway.common.exception.FilterErrorCase.TOO_MANY_WAITING_USERS;
import static com.ticketPing.gateway.common.exception.FilterErrorCase.WORKING_QUEUE_TOKEN_NOT_FOUND;

import com.ticketPing.gateway.application.service.QueueCheckService;
import com.ticketPing.gateway.common.utils.ResponseWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.security.core.Authentication;
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
    private final ResponseWriter responseWriter;

    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        logRequestDetails(exchange);


        ReactiveSecurityContextHolder.getContext()
                .map(context -> {
                    Authentication authentication = context.getAuthentication();
                    String userId = authentication.getName();
                    // 로직 수행
                    System.out.println(authentication);
                    System.out.println(userId);
                    return null;
                });

        String userId = "userId";
        String performanceId = exchange.getRequest().getQueryParams().getFirst(PERFORMANCE_ID_PARAM);
        APIType api = APIType.findByRequest(exchange.getRequest().getURI().getPath(), exchange.getRequest().getMethod().name());

        if (api == null) {
            return chain.filter(exchange);
        }
        return handleApiRequest(api, exchange, chain, userId, performanceId);
    }

    private void logRequestDetails(ServerWebExchange exchange) {
        log.info("요청 API 경로: {}, HTTP 메서드: {}",
                exchange.getRequest().getURI().getPath(),
                exchange.getRequest().getMethod().name());
    }

    private Mono<Void> handleApiRequest(APIType api, ServerWebExchange exchange, GatewayFilterChain chain, String userId, String performanceId) {
        return switch (api) {
            case ENTER_WAITING_QUEUE -> handleEnterWaitingQueueAPI(exchange, chain, performanceId);
            case GET_QUEUE_INFO -> handleGetQueueTokenAPI(exchange, chain, performanceId);
            case CREATE_ORDER, REQUEST_PAYMENT -> handleReservationAPI(exchange, chain, userId, performanceId);
        };
    }

    // 대기열 진입 API
    private Mono<Void> handleEnterWaitingQueueAPI(ServerWebExchange exchange, GatewayFilterChain chain, String performanceId) {
        if (queueCheckService.checkPerformanceSoldOut(performanceId)) {
            return responseWriter.setErrorResponse(exchange, PERFORMANCE_SOLD_OUT);
        }
        if (queueCheckService.checkTooManyWaitingUsers(performanceId)) {
            return responseWriter.setErrorResponse(exchange, TOO_MANY_WAITING_USERS);
        }
        return chain.filter(exchange);
    }

    // 대기열 상태 조회 API
    private Mono<Void> handleGetQueueTokenAPI(ServerWebExchange exchange, GatewayFilterChain chain, String performanceId) {
        if (queueCheckService.checkPerformanceSoldOut(performanceId)) {
            return responseWriter.setErrorResponse(exchange, PERFORMANCE_SOLD_OUT);
        }
        return chain.filter(exchange);
    }

    // 예매 API
    private Mono<Void> handleReservationAPI(ServerWebExchange exchange, GatewayFilterChain chain, String userId, String performanceId) {
        if (queueCheckService.checkPerformanceSoldOut(performanceId)) {
            return responseWriter.setErrorResponse(exchange, PERFORMANCE_SOLD_OUT);
        }
        if (!queueCheckService.checkUserAvailable(userId, performanceId)) {
            return responseWriter.setErrorResponse(exchange, WORKING_QUEUE_TOKEN_NOT_FOUND);
        }
        return chain.filter(exchange);
    }

}