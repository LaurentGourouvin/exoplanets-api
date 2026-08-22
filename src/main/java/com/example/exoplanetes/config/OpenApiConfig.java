package com.example.exoplanetes.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI exoplanetesOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Exoplanètes API")
                        .description("API REST de gestion d'observatoires astronomiques et d'exoplanètes. "
                                + "Projet de révision Spring Boot / JPA-Hibernate.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Laurent Gourouvin")
                                .url("https://github.com/LaurentGourouvin"))
                        .license(new License()
                                .name("MIT")
                                .url("https://opensource.org/licenses/MIT")));
    }
}