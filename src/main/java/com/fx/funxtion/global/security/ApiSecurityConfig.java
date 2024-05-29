package com.fx.funxtion.global.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class ApiSecurityConfig {

    private final JwtAuthorizationFilter jwtAuthorizationFilter;

    @Bean
    SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/**") // 이 url과 일치할때 검사하겠다는 의미
                .authorizeHttpRequests(
                        authorizeRequests -> authorizeRequests
                                .requestMatchers("/api/*/members").permitAll()
                                .requestMatchers("/api/*/members/*").permitAll()
                                .requestMatchers("/api/*/members/*/**").permitAll()
                                .requestMatchers("/api/*/notices").permitAll()
                                .requestMatchers("/api/*/notices/*").permitAll()
                                .requestMatchers("/api/*/notices/*/**").permitAll()
                                .requestMatchers("/api/*/faqs").permitAll()
                                .requestMatchers("/api/*/faqs/*").permitAll()
                                .requestMatchers("/api/*/faqs/*/**").permitAll()
                                .requestMatchers(HttpMethod.PUT, "/api/*/members/update").permitAll()
                                .requestMatchers("/api/*/products").permitAll()
                                .requestMatchers("/api/*/products/*").permitAll()
                                .requestMatchers("/api/*/products/*/**").permitAll()
                                .requestMatchers( HttpMethod.GET,"/api/*/chats").permitAll()
                                .requestMatchers("/api/*/chats/*").permitAll()
                                .requestMatchers("/api/*/chats/*/messages").permitAll()
                                .requestMatchers("/api/*/payments").permitAll()
                                .requestMatchers("/api/*/payments/*").permitAll()
                                .requestMatchers("/api/*/payments/*/**").permitAll()
                                .requestMatchers("/api/*/favorites").permitAll()
                                .requestMatchers("/api/*/notify").permitAll()
                                .requestMatchers("/api/*/notify/*").permitAll()
                                .requestMatchers("/api/*/notify/*/**").permitAll()
                                .requestMatchers("/api/*/reviews").permitAll()
                                .requestMatchers("/api/*/follow").permitAll()
                                .requestMatchers("/api/*/follow/*").permitAll()
                                .anyRequest().authenticated()
                )
                .csrf(
                        csrf -> csrf.disable()
                ) // csrf 토큰 끄기 (csrf 토큰 검사하는걸 끄겠다)
                .httpBasic(
                        httpBasic -> httpBasic.disable()
                ) // httpBasic 로그인 방식 끄기
                .formLogin(
                        formLogin -> formLogin.disable()
                ) // 폼 로그인 방식 끄기
                .sessionManagement(
                        sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                ) // 세션 끄기
                .addFilterBefore(
                        jwtAuthorizationFilter, // 액세스 토큰을 이용한 로그인 방식
                        UsernamePasswordAuthenticationFilter.class
                );
        return http.build();
    }
}
