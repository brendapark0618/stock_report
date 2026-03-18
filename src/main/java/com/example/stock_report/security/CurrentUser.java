package com.example.stock_report.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CurrentUser { // 현재 인증된 사용자의 정보를 담는 클래스
    private final Long id;
    private final String username;
}