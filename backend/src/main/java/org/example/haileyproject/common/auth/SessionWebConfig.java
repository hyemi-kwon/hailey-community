package org.example.haileyproject.common.auth;

import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SessionWebConfig implements WebMvcConfigurer {

    private final SessionAuthInterceptor sessionAuthInterceptor;
    private final String[] allowedOrigins;

    public SessionWebConfig(
            SessionAuthInterceptor sessionAuthInterceptor,
            @Value("${cors.allowed-origins:http://localhost:8080,http://127.0.0.1:8080}") String allowedOrigins) {
        this.sessionAuthInterceptor = sessionAuthInterceptor;
        this.allowedOrigins = allowedOrigins.split("\\s*,\\s*");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(sessionAuthInterceptor) // 1. "sessionAuthInterceptor  문지기"
                .addPathPatterns("/**")                 //
                .excludePathPatterns("/users/**", "/auth/**", "/posts/**", "/health");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(allowedOrigins)
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
