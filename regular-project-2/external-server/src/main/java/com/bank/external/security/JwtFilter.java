package com.bank.external.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

// 컨트롤러보다 먼저 실행되어, 토큰에서 "누구 / 무슨 역할"을 꺼내 보관함에 넣는다
public class JwtFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    public JwtFilter(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            try {
                Claims claims = jwtProvider.parse(token);
                Long userId = Long.valueOf(claims.getSubject());
                String role = claims.get("role", String.class);

                // principal = 회원 번호, credentials = 원본 토큰(내부 서버로 전달할 때 사용)
                var auth = new UsernamePasswordAuthenticationToken(
                        userId, token, List.of(new SimpleGrantedAuthority("ROLE_" + role)));
                auth.setDetails(claims.get("loginId", String.class));
                SecurityContextHolder.getContext().setAuthentication(auth);
            } catch (Exception e) {
                SecurityContextHolder.clearContext();   // 위조·만료 토큰은 로그인 안 한 것으로 취급
            }
        }
        chain.doFilter(request, response);
    }
}
