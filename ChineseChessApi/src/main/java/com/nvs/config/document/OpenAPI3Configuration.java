package com.nvs.config.document;

import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!prod")
public class OpenAPI3Configuration {

  @Bean
  public GroupedOpenApi publicApi(@Value("${openapi.service.api-docs}") String apiDocs) {
    return GroupedOpenApi.builder()
        .group(apiDocs)
        .packagesToScan("com.nvs.controller")
        .build();
  }

  @Bean
  public OpenAPI openAPI(@Value("${openapi.service.title}") String title,
      @Value("${openapi.service.version}") String version,
      @Value("${openapi.service.description}") String description,
      @Value("${openapi.service.serverUrl}") String serverUrl,
      @Value("${openapi.service.serverName}") String serverName) {
    return new OpenAPI().info(new Info().title(title)
            .version(version)
            .description(description)
            .license(new License().name("API License").url("http://domain.vn/license")))
        .servers(List.of(new Server().url(serverUrl).description(serverName)))
        .components(new Components()
            .addParameters("X-Custom-Header", new Parameter()
                .name("X-Custom-Header")
                .description("Custom header for this API")
                .required(false)
                .in("header"))
            .addParameters("Accept-Language", new Parameter()
                .name("Accept-Language")
                .description("Language to be used for the response")
                .required(false)
                .in(ParameterIn.HEADER.toString())
                .schema(new io.swagger.v3.oas.models.media.StringSchema()
                    ._default("en")
                    .addEnumItem("en")
                    .addEnumItem("vi")))
            .addSecuritySchemes("bearerAuth",
                new SecurityScheme()
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT"))
        );
  }
}
