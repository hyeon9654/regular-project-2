package com.bank.external.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

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
                .httpBasic(basic -> basic.disable())

                // 문 규칙
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**", "/csrf", "/error").permitAll() // 로그인, CSRF 쪽지, 에러 응답은 누구나
                        .requestMatchers("/api/customer/**").hasRole("CUSTOMER")            // 고객문
                        .requestMatchers("/api/staff/**").hasRole("STAFF")                  // 직원문
                        .anyRequest().denyAll())                                              // 위에 없는 주소는 전부 거절
                .exceptionHandling(ex->ex.                 // 로그인 안한 사람이 잠긴문 들어오면 401 에러
                        authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)));

        return http.build();
    }

    // 비밀번호(간편비밀번호 포함)를 BCrypt로 갈아서 저장, 비교하는 기계
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
