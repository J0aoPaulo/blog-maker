package org.acelera.blogmaker.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
        name = "BearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
@OpenAPIDefinition(
        info = @Info(
                title = "BlogMaker API",
                version = "1.0",
                description = "API BlogMaker - Um blog para desenvolvedores",
                contact = @Contact(
                        name = "João Paulo Almeida",
                        email = "contato.joaopaulodeveloper@gmail.com"
                )
        ),
        security = {@SecurityRequirement(name = "BearerAuth")},
        servers = {
                @Server(url = "http://localhost:8080", description = "Servidor local")
        }
)
public class SwaggerConfig {}
