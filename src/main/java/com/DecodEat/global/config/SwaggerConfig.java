package com.DecodEat.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI DecodEatAPI() {
        // 1. API 정보를 담는 Info 객체 생성
        Info info = new Info()
                .title("DecodEat API") // API 이름
                .description("DecodEat API 명세서입니다.") // API 설명
                .version("1.0.0"); // API 버전

        // 2. JWT 인증 설정을 위한 SecurityScheme 정의
        String jwtSchemeName = "JWT Bearer Token";
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwtSchemeName);
        Components components = new Components()
                .addSecuritySchemes(jwtSchemeName, new SecurityScheme()
                        .name(jwtSchemeName)
                        .type(SecurityScheme.Type.HTTP) // HTTP 타입
                        .scheme("bearer") // bearer 사용
                        .bearerFormat("JWT")); // 토큰 형식은 JWT

        // 3. OpenAPI 객체 생성 및 설정
        return new OpenAPI()
                .info(info)
                .addServersItem(new Server().url("/")) // API 서버의 기본 URL
                .addSecurityItem(securityRequirement)
                .components(components);
    }

    @Bean
    public GroupedOpenApi DecodEatApiGroup() {
        // "/api/**" 경로에 해당하는 API들을 그룹화
        String[] pathsToMatch = {"/api/**"};

        return GroupedOpenApi.builder()
                .group("DecodEat API") // 그룹 이름
                .displayName("DecodEat 전체 API") // UI에 표시될 그룹 이름
                .pathsToMatch(pathsToMatch) // 그룹에 포함시킬 경로
                .build();
    }
}