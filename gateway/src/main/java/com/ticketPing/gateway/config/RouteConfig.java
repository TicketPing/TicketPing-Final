package com.ticketPing.gateway.config;

import com.ticketPing.gateway.config.filter.QueueCheckFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RouteConfig {

    private final QueueCheckFilter queueCheckFilter;

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()

                // API Routing
                .route("auth-service", r -> r.path("/api/v1/auth/**")
                        .uri("lb://auth"))
                .route("user-service", r -> r.path("/api/v1/users/**")
                        .uri("lb://user"))
                .route("performance-service", r -> r.path(
                        "/api/v1/performances/**", "/api/v1/schedules/**", "/api/v1/seats/**")
                        .uri("lb://performance"))
                .route("order-service", r -> r.path("/api/v1/orders/**")
                        .filters(f -> f.filter(queueCheckFilter::filter))
                        .uri("lb://order"))
                .route("payment-service", r -> r.path("/api/v1/payments/**")
                        .filters(f -> f.filter(queueCheckFilter::filter))
                        .uri("lb://payment"))
                .route("queue-manage-service", r -> r.path(
                        "/api/v1/waiting-queue/**", "/api/v1/working-queue/**")
                        .filters(f -> f.filter(queueCheckFilter::filter))
                        .uri("lb://queue-manage"))

                // Swagger Routing
                .route("auth-docs", r -> r.path("/v3/api-docs/auth-service")
                        .filters(f -> f.rewritePath("/v3/api-docs/auth-service", "/v3/api-docs"))
                        .uri("lb://auth"))
                .route("user-docs", r -> r.path("/v3/api-docs/user-service")
                        .filters(f -> f.rewritePath("/v3/api-docs/user-service", "/v3/api-docs"))
                        .uri("lb://user"))
                .route("performance-docs", r -> r.path("/v3/api-docs/performance-service")
                        .filters(f -> f.rewritePath("/v3/api-docs/performance-service", "/v3/api-docs"))
                        .uri("lb://performance"))
                .route("order-docs", r -> r.path("/v3/api-docs/order-service")
                        .filters(f -> f.rewritePath("/v3/api-docs/order-service", "/v3/api-docs"))
                        .uri("lb://order"))
                .route("payment-docs", r -> r.path("/v3/api-docs/payment-service")
                        .filters(f -> f.rewritePath("/v3/api-docs/payment-service", "/v3/api-docs"))
                        .uri("lb://payment"))
                .route("queue-manage-docs", r -> r.path("/v3/api-docs/queue-manage-service")
                        .filters(f -> f.rewritePath("/v3/api-docs/queue-manage-service", "/v3/api-docs"))
                        .uri("lb://queue-manage"))

                .build();
    }

}
