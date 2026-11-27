package com.bank.external.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtProvider jwtProvider) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**", "/error").permitAll()     // 로그인과 오류 응답은 누구나
                .requestMatchers("/api/customer/**").hasRole("CUSTOMER")   // 고객 기능
                .anyRequest().denyAll())                                   // 그 밖의 주소는 전부 거절 (직원 기능은 이 서버에 없다)
            .addFilterBefore(new JwtFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
