package com.lemon.mdcord.common.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "MDCODE API 명세서",
//                description = "API 명세서",
                version = "v1"
//                contact = @Contact(
//                        name = "담당자명",
//                        email = "담당자이메일"
//                )
        )
)
@Configuration
public class SwaggerConfig {

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