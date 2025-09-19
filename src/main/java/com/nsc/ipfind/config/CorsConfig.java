package com.nsc.ipfind.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;

/**
 * 全局跨域配置
 * 解决前端页面（如 http://lnkwang.me）访问后端接口（如 /api/auth/login）时的 CORS 问题
 */
@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(Arrays.asList("http://39.97.174.238:8080")); // 允许的源
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(Arrays.asList("*"));
        config.setAllowCredentials(true); // 是否允许携带凭证（如 Cookie）
        config.setMaxAge(3600L); // 预检请求缓存时间（秒）

        source.registerCorsConfiguration("/api/**", config);
        return source;
    }
}
