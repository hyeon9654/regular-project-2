package com.bank.external.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

// 토큰을 만들고(로그인 때) 읽는(요청마다) 도구
@Component
public class JwtProvider {

    private static final long EXPIRE_MILLIS = 30 * 60 * 1000;   // 30분

    private final SecretKey key;

    public JwtProvider(@Value("${jwt.secret}") String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    // 로그인 성공 시: 누구인지(userId), 어떤 아이디인지(loginId), 역할(role)을 담아 발급
    public String create(Long userId, String loginId, String role) {
        Date now = new Date();
        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("loginId", loginId)
                .claim("role", role)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + EXPIRE_MILLIS))
                .signWith(key)
                .compact();
    }

    // 요청마다: 서명과 만료를 검사하고 내용을 꺼낸다 (위조·만료면 예외)
    public Claims parse(String token) {
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
    }
}
