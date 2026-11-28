package com.bank.external.auth;

import com.bank.external.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {

    DEVICE_NOT_REGISTERED(HttpStatus.UNAUTHORIZED, "등록되지 않은 기기입니다"),                  // 401
    PIN_LOCKED(HttpStatus.LOCKED, "간편비밀번호가 잠겼습니다. 본인 확인 후 다시 설정해 주세요"),      // 423
    PIN_MISMATCH(HttpStatus.UNAUTHORIZED, "간편비밀번호가 일치하지 않습니다");                    // 401

    private final HttpStatus status;            // 응답에 쓸 HTTP 상태
    private final String message;               // 앱에 보여 줄 문구


}
