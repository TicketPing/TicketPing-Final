package com.ticketPing.gateway.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SwaggerConfig {

    private final RouteLocator routeLocator;

    @Bean
    public List<GroupedOpenApi> apis() {
        List<GroupedOpenApi> groups = new ArrayList<>();

        Objects.requireNonNull(routeLocator.getRoutes().collectList().block()).stream()
                .filter(route -> route.getId() != null && !route.getId().contains("-docs")) // API 문서 라우트 제외
                .forEach(route -> {
                    String name = route.getId();
                    String path = "/services/" + name.toLowerCase() + "/v3/api-docs";

                    groups.add(GroupedOpenApi.builder()
                            .pathsToMatch(path)
                            .group(name)
                            .build());
                });

        return groups;
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("TicketPing API")
                        .version("1.0"));
    }

}