package com.bank.external.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode{ // 기능과 상관없는 공통 실패 목록
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "잘못된 입력입니다"),             // 400: @Valid 검사 실패
    NOT_FOUND(HttpStatus.NOT_FOUND, "요청한 주소를 찾을 수 없습니다"),        // 404: 없는 주소
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류입니다");    // 500: 예상 못 한 에러 (버그)

    private final HttpStatus status;
    private final String message;
}
