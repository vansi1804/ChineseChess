// package com.config.apiDocument;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.web.bind.annotation.RestController;

// import springfox.documentation.builders.ApiInfoBuilder;
// import springfox.documentation.builders.PathSelectors;
// import springfox.documentation.builders.RequestHandlerSelectors;
// import springfox.documentation.service.ApiInfo;
// import springfox.documentation.service.Contact;
// import springfox.documentation.spi.DocumentationType;
// import springfox.documentation.spring.web.plugins.Docket;
// import springfox.documentation.swagger2.annotations.EnableSwagger2;

// @Configuration
// @EnableSwagger2
// public class Swagger2Configuration {

//     @Bean
//     public Docket api() {
//         return new Docket(DocumentationType.SWAGGER_2).select()
//                 .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
//                 // .apis(RequestHandlerSelectors.basePackage("com.controller"))
//                 .paths(PathSelectors.any())
//                 .build().apiInfo(apiEndPointsInfo());
//     }

//     private ApiInfo apiEndPointsInfo() {
//         return new ApiInfoBuilder().title("Spring Boot REST API")
//                 .description("Chinese Chess REST API")
//                 .contact(new Contact("KnightS", "", "vansi18042001@gmail.com"))
//                 .license("Apache 2.0")
//                 .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html")
//                 .version("1.0.0")
//                 .build();
//     }

// }
