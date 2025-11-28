package com.rutaflex.trackflexv3.config;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private static final String SECURITY_SCHEME_NAME = "bearerAuth";

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                // Define el esquema de seguridad (Bearer Token)
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME,
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .in(SecurityScheme.In.HEADER)
                                        .name("Authorization")))
                // Aplica el esquema de seguridad a todos los endpoints por defecto
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                // Información general de la API (opcional si ya usaste application.yml)
                .info(new Info()
                        .title("TrackFlex - Sistema de Gestión Logística API")
                        .version("1.0.0")
                        .description("Documentación de la API con soporte para JWT."));
    }
}