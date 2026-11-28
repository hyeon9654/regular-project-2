package com.bank.external.common.exception;

import org.springframework.http.ResponseEntity;

public record ErrorResponse(String code, String message) {  // 앱이 받는 JSON 모양 {code, message}

    public static ResponseEntity<ErrorResponse> of(ErrorCode e) {          // ErrorCode 하나로 응답 상자를 만드는 도우미
        return ResponseEntity.status(e.getStatus())                        // ResponseEntity : 상태코드 + 본문을 담는 응답 상자
                .body(new ErrorResponse(e.name(), e.getMessage()));        // 본문: code = enum 이름, message = 문구
    }
}
