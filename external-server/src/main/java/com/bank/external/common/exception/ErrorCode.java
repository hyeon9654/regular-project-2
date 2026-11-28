package com.bank.external.common.exception;

import org.springframework.http.HttpStatus;

// 모든 기능의 에러 코드가 지킬 약속
public interface ErrorCode {
    String name();                  // 코드 이름 (예: PIN_LOCKED). enum이면 자동으로 있음

    HttpStatus getStatus();         //  프링 제공 HttpStatus: 응답 상태 (401, 423 ...)

    String getMessage();            // 앱에 보여 줄 기본 문구
}
