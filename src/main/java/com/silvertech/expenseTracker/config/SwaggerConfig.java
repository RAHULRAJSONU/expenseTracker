package com.silvertech.expenseTracker.config;

import com.google.common.base.Predicate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static com.google.common.base.Predicates.or;
import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(or(
                        RequestHandlerSelectors.withMethodAnnotation(RestResource.class),
                        RequestHandlerSelectors.withClassAnnotation(RepositoryRestController.class)))
                .paths(PathSelectors.any())
                .build();
    }

/*    private Predicate<String> getPath() {
        return or(
                regex("/transaction-logs/.*")
        );
    }*/
}
