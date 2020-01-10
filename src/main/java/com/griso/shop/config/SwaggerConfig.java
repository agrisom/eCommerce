package com.griso.shop.config;

import com.google.common.collect.Lists;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket apiCommon() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("1. Client Services")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.griso.shop.controller.client"))
                .paths(PathSelectors.any())
                .build()
                .useDefaultResponseMessages(false)
                .pathMapping("/")
                .securitySchemes(Lists.newArrayList(apiKey()))
                .apiInfo(metaDataClient());
    }

    @Bean
    public Docket apiAdmin() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("2. Admin Services")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.griso.shop.controller.admin"))
                .paths(PathSelectors.any())
                .build()
                .useDefaultResponseMessages(false)
                .pathMapping("/")
                .securitySchemes(Lists.newArrayList(apiKey()))
                .apiInfo(metaDataAdmin());
    }

    private ApiKey apiKey() {
        return new ApiKey("Bearer", "Authorization", "header");
    }

    private ApiInfo metaDataClient(){

        Contact contact = new Contact("Albert Griso Mendez", "https://github.com/agrisom",
                "albert.griso.mendez@gmail.com");

        return new ApiInfo(
                "eCommerce Spring RESTFull - Client Services",
                "Services used on the client side for the eCommerce",
                "0.0.1-SNAPSHOT",
                null,
                contact,
                "Apache License Version 2.0",
                "https://www.apache.org/licenses/LICENSE-2.0",
                new ArrayList<>());
    }

    private ApiInfo metaDataAdmin(){

        Contact contact = new Contact("Albert Griso Mendez", "https://github.com/agrisom",
                "albert.griso.mendez@gmail.com");

        return new ApiInfo(
                "eCommerce Spring RESTFull - Data Administration Services",
                "Services used by the admin user to provide all eCommerce data",
                "0.0.1-SNAPSHOT",
                null,
                contact,
                "Apache License Version 2.0",
                "https://www.apache.org/licenses/LICENSE-2.0",
                new ArrayList<>());
    }
}