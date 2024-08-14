package com.nvs.config.document;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import java.util.Collections;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPI3Configuration {

  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI().servers(
            Collections.singletonList(new Server().url("http://localhost:8088/api/open-api-docs")))
        .info(
            new Info().title("Chinese Chess Application API").description("OpenAPI 3.0")
                .contact(new Contact().email("vansi18042001@gmail.com").name("nvsi01")).license(
                    new License().name("Apache 2.0")
                        .url("http://www.apache.org/licenses/LICENSE-2.0.html"))
                .version("1.0.0"));
  }

}
