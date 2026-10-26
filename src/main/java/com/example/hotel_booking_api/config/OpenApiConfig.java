package com.example.hotel_booking_api.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
    final String securitySchemaName = "bearerAuth";

        return new OpenAPI()
                .info(new Info()
                        .title("Hotel Booking API")
                        .description("REST API for Hotel and Reservation Management")
                        .version("1.0.0"))
                .tags(List.of(
                        new Tag().name("Authentication").description("User registration and login"),
                        new Tag().name("Users").description("User details, role assignment, and account deletion"),
                        new Tag().name("Hotels").description("Hotel CRUD operations and manager assignment"),
                        new Tag().name("Rooms").description("Room CRUD operations within hotels"),
                        new Tag().name("Reservations").description("Booking creation, viewing, and cancellation")
                ))
            .addSecurityItem(new SecurityRequirement().addList(securitySchemaName))
            .components(new Components()
                        .addSecuritySchemes(securitySchemaName,
                                new SecurityScheme()
                                        .name(securitySchemaName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));
    }
}
