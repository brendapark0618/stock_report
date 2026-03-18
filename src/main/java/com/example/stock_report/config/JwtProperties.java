package com.example.stock_report.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app.jwt")
public class JwtProperties { // application.yaml의 app.jwt 섹션과 매핑되는 클래스
    private String secret;
    private String issuer;
    private String cookieName;
}