package com.fx.funxtion.global.util.mail.dto;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties("application.mail") // application.yml
public class MailProperties {
    // SMTP 서버
    private String host;

    // 포트번호
    private int port;

    // 계정
    private String username;

    // 비밀번호
    private String password;

    private String fromMail;
}
