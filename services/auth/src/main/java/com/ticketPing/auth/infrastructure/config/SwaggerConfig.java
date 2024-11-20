package com.ticketPing.auth.infrastructure.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Collections;

@Configuration
public class SwaggerConfig {
        @Bean
        public OpenAPI customOpenAPI() {
                return new OpenAPI()
                        .info(new Info()
                                .title("Auth Service API")
                                .version("1.0"))
                        .components(
                                new Components()
                                        .addSecuritySchemes("Authorization", new SecurityScheme()
                                                .name("Authorization")
                                                .type(SecurityScheme.Type.APIKEY)
                                                .in(SecurityScheme.In.HEADER)
                                                .bearerFormat("JWT")
                                        )
                        )
                        .security(Collections.singletonList(
                                new SecurityRequirement().addList("Authorization")
                        ));
        }
}
