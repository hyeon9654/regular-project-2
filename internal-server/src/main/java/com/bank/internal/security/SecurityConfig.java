package com.bank.internal.security;

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
                .requestMatchers("/staff/auth/**", "/error").permitAll()   // 직원 로그인과 오류 응답
                .requestMatchers("/staff/**").hasRole("STAFF")             // 직원 업무
                .requestMatchers("/internal/**").hasRole("CUSTOMER")       // 외부 서버가 고객 토큰과 함께 호출
                .anyRequest().denyAll())
            .addFilterBefore(new JwtFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
