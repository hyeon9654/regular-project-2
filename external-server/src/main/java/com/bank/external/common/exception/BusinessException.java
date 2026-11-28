package com.bank.external.common.exception;

import lombok.Getter;

// "이 요청은 업무 규칙에 걸려서 실패했다"고 알리는 예외, 안에 어떤 실패인지(ErrorCode)를 담아서 던진다
@Getter
public class BusinessException extends RuntimeException{

    private final ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode){
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
