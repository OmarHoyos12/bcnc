package com.inditex.coreplatform.infrastructure.config;

import io.swagger.v3.parser.OpenAPIV3Parser;
import io.swagger.v3.oas.models.OpenAPI;
import org.apache.commons.io.IOUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import com.inditex.coreplatform.exception.OpenApiConfigurationException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        try (InputStream inputStream = new ClassPathResource("openapi.yml").getInputStream()) {
            String openApiContent = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            return new OpenAPIV3Parser().readContents(openApiContent, null, null).getOpenAPI();
        } catch (IOException e) {
            throw new OpenApiConfigurationException("Failed to load openapi.yml", e);
        }
    }
}
