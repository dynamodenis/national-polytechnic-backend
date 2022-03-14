package com.mabawa.nnpdairy.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

//@Configuration
//public class WebConfig {
//    private static final long MAX_AGE_SEC = 3600L;
//
//    @Bean
//    CorsConfigurationSource corsConfigurationSource() {
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", (new CorsConfiguration()).applyPermitDefaultValues());
//        return source;
//    }
//
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**").allowedOrigins(new String[]{"*"}).allowedMethods(new String[]{"HEAD", "GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"}).exposedHeaders(new String[]{"Authorization"}).maxAge(3600L);
//    }
//}
