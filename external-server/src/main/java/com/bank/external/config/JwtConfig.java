package com.bank.external.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Configuration
public class JwtConfig {

    @Value("${jwt.secret}")
    private String secret;

    // 비밀키 글자 -> 도장용 열쇠 (스프링이 보관, 인코더/디코더가 받아 쓴다)
    @Bean
    public SecretKey jwtSecretKey(){
        byte[] bytes = Base64.getDecoder().decode(secret);      // Base64 글자를 진짜 바이트로 디코드해서 보관
        return new SecretKeySpec(bytes, "HmacSHA256"); // 바이트를 해당 대칭키알고리즘 도장용 열쇠로 포장해서 돌려주기
    }

    // 도장 확인 기계 (경비원이 요청마다 토큰이 진짜인지, 만료됐는지 검사할때 쓴다)
    @Bean
    public JwtDecoder jwtDecoder(SecretKey secretKey){
        return NimbusJwtDecoder
                .withSecretKey(secretKey)           // 위에 만들어둔 SecretKey 열쇠로 도장을 확인
                .macAlgorithm(MacAlgorithm.HS256)   // HS256 도장만 받기 (다른 방식은 거절)
                .build();                           // NimbusJwtDecoder -> JWT 확인 기계 빌드
    }

    // 도장 찍는 기계 (로그인 성공 때 토큰을 만들 때 쓴다)
    @Bean
    public JwtEncoder jwtEncoder(SecretKey secretKey){
        return NimbusJwtEncoder
                .withSecretKey(secretKey)   // 위에 만들어준 SecretKey 열쇠로 도장을 찍는다
                .build();                   // NimbusJwtEncoder -> JWT 도장 찍는 기계 빌드
    }
}
