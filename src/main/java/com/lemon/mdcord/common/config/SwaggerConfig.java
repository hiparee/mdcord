package com.lemon.mdcord.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .version("v1.0.0")
                .title("MDCODE API 명세서");

        String jwtSchemeName = "Authentication";
        SecurityRequirement requirement = new SecurityRequirement().addList(jwtSchemeName);
        Components components = new Components()
                .addSecuritySchemes(jwtSchemeName, new SecurityScheme()
                        .name(jwtSchemeName)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                );

        return new OpenAPI()
                .info(info)
                .addSecurityItem(requirement)
                .components(components);
    }

    @Bean
    public GroupedOpenApi memberOpenApi() {
        String[] paths = {"/api/members/**"};

        return GroupedOpenApi.builder()
                .group("사용자 관련 API")
                .pathsToMatch(paths)
                .build();
    }

    @Bean
    public GroupedOpenApi channelOpenApi() {
        String[] paths = {"/api/channel-list/**"};

        return GroupedOpenApi.builder()
                .group("채널 관련 API")
                .pathsToMatch(paths)
                .build();
    }

    @Bean
    public GroupedOpenApi chatOpenApi() {
        String[] paths = {"/api/chat/**"};

        return GroupedOpenApi.builder()
                .group("채팅 관련 API")
                .pathsToMatch(paths)
                .build();
    }

}