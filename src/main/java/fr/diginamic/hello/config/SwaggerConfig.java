package fr.diginamic.hello.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration Swagger pour générer de la documentation sur une API
 */
@Configuration
public class SwaggerConfig {
    /**
     * Configuration customisée pour Swagger
     * @return OpenAPI
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("REST API de recensement - France")
                        .version("1.0")
                        .description("Cette application fournit des données sur les communes et départements de France."));
    }
}
