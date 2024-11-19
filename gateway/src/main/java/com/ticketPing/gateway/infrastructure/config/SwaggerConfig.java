package com.ticketPing.gateway.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.properties.SwaggerUiConfigParameters;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
@RequiredArgsConstructor
public class SwaggerConfig {

    private final RouteLocator routeLocator;

    @Bean
    public CommandLineRunner openApiGroups(SwaggerUiConfigParameters swaggerUiParameters) {
        return args -> Objects.requireNonNull(routeLocator.getRoutes().collectList().block())
                .stream()
                .filter(route -> route.getId() != null && !route.getId().contains("-docs")) // API 문서 라우트 제외
                .forEach(route -> {
                    String name = route.getId();
                    swaggerUiParameters.addGroup(name);
                });
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("TicketPing API")
                        .version("1.0"));
    }

}