package com.bank.external.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CustomerLoginRequest(
        @NotBlank
        String deviceUuid,

        @NotBlank                       // 공백 거절
        @Pattern(regexp = "\\d{6}")     // 숫자 6자리만 허용 패턴
        String pin
) {
}


/*
 *** record는 값을 담기만 하는 클래스를 짧게 만드는 문법 -> 아래 내용을 간단하게 쓸때 record 쓴다 ***

public final class CustomerLoginRequest {          // 클래스도 final -> 상속 불가
    private final String deviceUuid;               // 필드: private final 자동
    private final String pin;

    public CustomerLoginRequest(String deviceUuid, String pin) {  // 모든 필드를 받는 생성자
        this.deviceUuid = deviceUuid;
        this.pin = pin;
    }

    public String deviceUuid() { return deviceUuid; }  // 읽기 메서드 (get 없이 이름 그대로)
    public String pin() { return pin; }

    // equals(), hashCode(), toString()도 자동
}
*/
