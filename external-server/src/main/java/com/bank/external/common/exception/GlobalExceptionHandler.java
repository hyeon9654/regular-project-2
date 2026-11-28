package com.bank.external.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@RestControllerAdvice // 모든 컨트롤러의 예외를 받는 창구
public class GlobalExceptionHandler {

    // [A] 우리가 던진 BusinessException -> 봉투 속 사유 그대로
    @ExceptionHandler(BusinessException.class) // 이 타입 예외가 오면 아래 메서드 실행
    public ResponseEntity<ErrorResponse> handleBusiness(BusinessException e) {
        return ErrorResponse.of(e.getErrorCode());
    }

    // [B] @Valid 검사 실패 -> 400
    @ExceptionHandler(MethodArgumentNotValidException.class) // @Valid 실패 시 스프링이 던지는 예외
    public ResponseEntity<ErrorResponse> handleValid(MethodArgumentNotValidException e) {
        return ErrorResponse.of(CommonErrorCode.INVALID_INPUT);
    }

    // [404] 없는 주소 -> 404
    @ExceptionHandler(NoResourceFoundException.class) // 맞는 컨트롤러가 없을 때 던지는 예외
    public ResponseEntity<ErrorResponse> handleNotFound(NoResourceFoundException e) {
        return ErrorResponse.of(CommonErrorCode.NOT_FOUND);
    }

    // [C] 나머지 전부 (버그) -> 500, 자세한 내용은 서버 로그에만
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAll(Exception e) {
        log.error("처리하지 못한 예외", e);
        return ErrorResponse.of(CommonErrorCode.INTERNAL_ERROR);
    }

    // [B-2] JSON 모양이 깨짐 -> 400
    @ExceptionHandler(HttpMessageNotReadableException.class)   // 요청 본문을 읽지 못했을 때 던지는 예외
    public ResponseEntity<ErrorResponse> handleNotReadable(HttpMessageNotReadableException e) {
        return ErrorResponse.of(CommonErrorCode.INVALID_INPUT);
    }
}
