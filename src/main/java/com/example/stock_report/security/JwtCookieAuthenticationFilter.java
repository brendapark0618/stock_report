package com.example.stock_report.security;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.stock_report.config.JwtProperties;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

/*
이 필터는 요청마다 한 번만 실행되는 OncePerRequestFilter 기반이고, JWT를 검증한 뒤 SecurityContext에 인증 객체를 넣음.
*/

@Component
@RequiredArgsConstructor
public class JwtCookieAuthenticationFilter extends OncePerRequestFilter { // 매 요청마다 실행되는 필터, JWT 토큰이 유효하면 SecurityContext에 인증 정보 넣어줌 (핵심 기능))

    private final JwtProperties jwtProperties;

    @Override
    protected void doFilterInternal( // 매 요청마다 실행되는 필터 메서드
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String token = extractCookieValue(request, jwtProperties.getCookieName());

        if (token != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                Algorithm algorithm = Algorithm.HMAC256(jwtProperties.getSecret()); // JWT 토큰 검증을 위한 알고리즘 설정
                JWTVerifier verifier = JWT.require(algorithm)
                        .withIssuer(jwtProperties.getIssuer())
                        .build();

                DecodedJWT decodedJWT = verifier.verify(token);

                String subject = decodedJWT.getSubject();
                String username = decodedJWT.getClaim("username").asString();

                if (subject != null && username != null) {
                    CurrentUser currentUser = new CurrentUser(Long.valueOf(subject), username); // JWT 토큰의 subject를 사용자 ID로, claim의 username을 사용자 이름으로 사용

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    currentUser,
                                    null,
                                    Collections.emptyList()
                            );

                    authentication.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }

            } catch (Exception e) {
                // 토큰이 잘못됐으면 인증 안 넣고 그냥 다음으로 넘김
                // 보호 API는 SecurityConfig에서 401 처리됨
                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request, response);
    }

    private String extractCookieValue(HttpServletRequest request, String cookieName) {
        if (request.getCookies() == null) {
            return null;
        }

        Optional<Cookie> cookie = Arrays.stream(request.getCookies())
                .filter(c -> cookieName.equals(c.getName()))
                .findFirst();

        return cookie.map(Cookie::getValue).orElse(null);
    }
}