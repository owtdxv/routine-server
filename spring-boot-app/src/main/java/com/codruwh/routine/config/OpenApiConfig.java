package com.codruwh.routine.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "API", description = "API 명세서", version = "v0"))
@SecurityScheme(
    name = "bearerAuth", // 보안 스킴의 고유한 이름 (나중에 참조할 때 사용)
    type = SecuritySchemeType.HTTP, // 보안 스킴의 타입 (HTTP)
    bearerFormat = "JWT", // 토큰 형식
    scheme = "bearer" // 사용하는 스킴 (Bearer)
)
public class OpenApiConfig {
}
