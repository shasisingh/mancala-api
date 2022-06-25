package com.shashi.bol.mancala.game.v1.config;

import org.reflections.Reflections;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Configuration
@EnableSwagger2
@EnableWebMvc
public class SwaggerConfig {

    private static final Set<String> DEFAULT_PRODUCES_AND_CONSUMES =
            new HashSet<>(List.of("application/json"));

    @Bean
    public Docket mancalaApi(ApiContact apiContact) {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.shashi.bol.mancala.game.v1.controller"))
                .build()
                .useDefaultResponseMessages(false)
                .ignoredParameterTypes(getModelClasses())
                .apiInfo(new ApiInfoBuilder().version("0.0.1")
                        .contact(apiContact.getApiContact())
                        .title("Mancala API").build())
                .tags(
                        new Tag("mancala board", "Services related with board")
                ).produces(DEFAULT_PRODUCES_AND_CONSUMES)
                .consumes(DEFAULT_PRODUCES_AND_CONSUMES);
    }

    @Bean
    public ApiContact getContact(){
        return new ApiContact();
    }


    private Class[] getModelClasses() {
        Reflections reflections = new Reflections("com.shashi.bol.mancala.game.v1.domain");
        Set<Class<?>> allClasses = reflections.getSubTypesOf(Object.class);
        return allClasses.toArray(Class[]::new);
    }
}
