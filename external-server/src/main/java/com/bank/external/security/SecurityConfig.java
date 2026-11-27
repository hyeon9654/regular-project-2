package com.bank.external.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    // 경비원 규칙표 (요청이 컨트롤러에 가기전에 거치는 검사 줄)
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                // CSRF 방어 켜기 (직원 웹 REACT용)
                .csrf(csrf -> csrf.spa())

                // 서버가 로그인 상태를 세션에 기억하지 않기
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 스프링 기본으로 띄우는 로그인 화면 끄기
                .formLogin(form -> form.disable())

                // 브라우저 팝업 로그인 끄기
                .httpBasic(basic -> basic.disable());

        return http.build();
    }

    // 비밀번호(간편비밀번호 포함)를 BCrypt로 갈아서 저장, 비교하는 기계
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
