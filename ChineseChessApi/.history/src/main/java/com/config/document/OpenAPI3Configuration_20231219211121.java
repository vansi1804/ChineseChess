package com.config.document;

import com.google.common.collect.Lists;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPI3Configuration {

  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
      // Setting up servers for testing API
      .servers(Lists.newArrayList(new Server().url("http://localhost:8088")))
      // Info
      .info(
        new Info()
          .title("Chinese Chess Application API")
          .description("OpenAPI 3.0")
          .contact(
            new Contact().email("vansi18042001@gmail.com").name("nvsi01")
          )
          .license(
            new License()
              .name("Apache 2.0")
              .url("http://www.apache.org/licenses/LICENSE-2.0.html")
          )
          .version("1.0.0")
      );
  }
}
