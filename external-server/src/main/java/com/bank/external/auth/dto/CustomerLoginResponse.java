package com.bank.external.auth.dto;

public record CustomerLoginResponse(
        String accessToken      // 로그인 성공 시 발급한 access 토큰 (15분)
) {
}
