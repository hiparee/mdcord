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
                .title("MDCORD API 명세서");

        String jwtSchemeName = "Authentication";
        SecurityRequirement requirement = new SecurityRequirement().addList(jwtSchemeName);
        Components components = new Components()
                .addSecuritySchemes(jwtSchemeName, new SecurityScheme()
                        .name(jwtSchemeName)
                        .type(SecurityScheme.Type.APIKEY)
                        .in(SecurityScheme.In.COOKIE)
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
        String[] paths = {"/api/channels/**"};

        return GroupedOpenApi.builder()
                .group("채널 관련 API")
                .pathsToMatch(paths)
                .build();
    }

    @Bean
    public GroupedOpenApi chatOpenApi() {
        String[] paths = {"/api/chat/**", "/api/channels/**/chat/**"};

        return GroupedOpenApi.builder()
                .group("채팅 관련 API")
                .pathsToMatch(paths)
                .build();
    }

    @Bean
    public GroupedOpenApi channelMemberOpenApi() {
        String[] paths = {"/api/channel-member/**"};

        return GroupedOpenApi.builder()
                .group("채널 멤버 관련 API")
                .pathsToMatch(paths)
                .build();
    }

    @Bean
    public GroupedOpenApi attachFileOpenApi() {
        String[] paths = {"/channels/**/attach-file/**", "/channels/**/image-file/**"};

        return GroupedOpenApi.builder()
                .group("첨부 파일 관련 API(CDN 서버 대용)")
                .pathsToMatch(paths)
                .build();
    }

}