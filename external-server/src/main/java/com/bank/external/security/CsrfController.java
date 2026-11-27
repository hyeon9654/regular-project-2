package com.bank.external.security;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// React 앱 시작 -> 프론트 코드에 "시작하면 /csrf 먼저 불러"라고 짜 둠 ,  GET /csrf -> XSRF-TOKEN 쿠키 저장
@RestController
public class CsrfController {

    // CSRF 쪽지(XSRF-TOKEN 쿠키)를 내려주는 문
    // 직원 웹(React)은 앱 켤 때 한 번 부르고, 그 뒤 POST 마다 쿠키 값을 X-XSRF-TOKEN 헤더에 넣어 보낸다
    @GetMapping("/csrf")
    public void csrf(CsrfToken csrfToken){
        csrfToken.getToken(); // 쪽지를 한 번 꺼내 봐야 응답에 XSRF-TOKEN 쿠키가 실린다
    }
}
