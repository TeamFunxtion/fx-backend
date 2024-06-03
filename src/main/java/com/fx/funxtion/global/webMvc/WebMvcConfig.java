package com.fx.funxtion.global.webMvc;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:3000", "https://fx-frontend-funxtion.vercel.app/")
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
