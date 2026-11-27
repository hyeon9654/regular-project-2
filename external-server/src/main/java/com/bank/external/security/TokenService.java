package com.bank.external.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class TokenService {

    // JwtConfig 에서 만든 도장 찍는 기계
    private final JwtEncoder jwtEncoder;

    @Value("${jwt.access-token-time}")  // 15분
    private Duration accessTokenTime;

    // accessToken 만들기
    public String createAccessToken(Long id, String role){
        Instant now = Instant.now();

        // claims -> 토큰에 적을 내용
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(String.valueOf(id))            // sub : 누구(고객번호 or 직원번호)
                .claim("role", role)              // role : "CUSTOMER" or "STAFF"
                .issuedAt(now)                          // iat : 발급 시각 (지금)
                .expiresAt(now.plus(accessTokenTime))   // exp : 만료 시각 (지금 + 15분)
                .build();

        // 헤더 -> "HS256" 방식으로 찍었다 라고 적기 (디코더랑 맞춰야 한다)
        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();

        // 헤더 + 내용을 넣고 인코딩 -> 완성된 토큰을 글자로 꺼내서 리턴
        return jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
    }
}
