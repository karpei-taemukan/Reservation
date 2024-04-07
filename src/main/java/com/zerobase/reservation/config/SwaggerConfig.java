package com.zerobase.reservation.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;

@Configuration
@RequiredArgsConstructor
@OpenAPIDefinition(
        info = @Info(title = "Reservation API",
                description = "Library Management Apis",
                version = "3.0"))
public class SwaggerConfig {


   @Bean
    public OpenAPI api() {

        SecurityScheme apiKey = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .in(SecurityScheme.In.HEADER)
                .name("Authorization")
                .scheme("bearer")
                .bearerFormat("JWT");

        return new OpenAPI().addSecurityItem(new SecurityRequirement()
                .addList("Bearer Authentication"))
                .components(new Components().addSecuritySchemes("Bearer Authentication", apiKey))
                .info(new io.swagger.v3.oas.models.info.Info().title("REST API")
                        .description("CUSTOMER API")
                        .version("1.0"));

        /*        return new OpenAPI().info(new io.swagger.v3.oas.models.info.Info()
                        .title("API title"))
                .components(new Components()
                        .addSecuritySchemes("bearer-key",
                                new SecurityScheme().type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));*/
    }

}
