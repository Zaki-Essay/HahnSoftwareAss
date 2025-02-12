package com.example.employeemanagementsystemapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.time.LocalDate;

@Configuration
public class SwaggerConfig {
        @Bean
        public Docket customImplementation() {

                return new Docket(DocumentationType.SWAGGER_2)
                        .select()
                        .paths(PathSelectors.any())
                        .apis(RequestHandlerSelectors.basePackage("com.example"))
                        .build()
                        .apiInfo(apiInfo())
                        .pathMapping("/")
                        .useDefaultResponseMessages(false)
                        .directModelSubstitute(LocalDate.class, String.class)
                        .genericModelSubstitutes(ResponseEntity.class);
        }

        ApiInfo apiInfo() {
                return new ApiInfoBuilder()
                        .title("Swagger with Spring Boot + Security")
                        .version("1.0.0")
                        .description("Your Description")
                        .contact(new Contact("zakariae ESSAIYDY", "Contact_URL","zakaressaiydy@gmail.com"))
                        .build();
        }
}