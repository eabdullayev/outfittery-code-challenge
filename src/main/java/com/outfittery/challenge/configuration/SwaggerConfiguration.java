package com.outfittery.challenge.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {
    @Bean
    public Docket customerApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("customer-api-v1.0")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.outfittery.challenge.controllers"))
                .paths(regex("/api/v1.0/customer/.*"))
                .build()
                .apiInfo(new ApiInfoBuilder()
                        .version("1.0")
                        .title("Customer API")
                        .description("CRUD operations for customers.")
                        .contact(new Contact("Elchin Abdullayev", "", "elchin.abd@gmail.com"))
                        .build());
    }

    @Bean
    public Docket reservationApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("reservation-api-v1.0")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.outfittery.challenge.controllers"))
                .paths(regex("/api/v1.0/reservation/.*"))
                .build()
                .apiInfo(new ApiInfoBuilder()
                        .version("1.0")
                        .title("Reservation API")
                        .description("API for making reservations and getting available time slots")
                        .contact(new Contact("Elchin Abdullayev", "", "elchin.abd@gmail.com"))
                        .build());
    }

    @Bean
    public Docket stylistApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("stylist-api-v1.0")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.outfittery.challenge.controllers"))
                .paths(regex("/api/v1.0/stylist/.*"))
                .build()
                .apiInfo(new ApiInfoBuilder()
                        .version("1.0")
                        .title("Stylist API")
                        .description("Stylist API for making request for leave, create and update")
                        .contact(new Contact("Elchin Abdullayev", "", "elchin.abd@gmail.com"))
                        .build());
    }
}
