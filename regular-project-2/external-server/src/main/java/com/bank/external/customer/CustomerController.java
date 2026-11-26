package com.bank.external.customer;

import com.bank.external.core.CoreClient;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// 고객 기능. 이 서버는 DB가 없으므로, 필요한 것은 내부 서버에 물어본다.
@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CoreClient coreClient;

    // 본인 계좌 조회
    @GetMapping("/accounts")
    public List<CoreClient.AccountDto> myAccounts(Authentication auth) {
        Long userId = (Long) auth.getPrincipal();        // JwtFilter 가 넣어 둔 회원 번호
        String token = (String) auth.getCredentials();   // JwtFilter 가 넣어 둔 원본 토큰
        return coreClient.getAccounts(token, userId);
    }

    // 앞으로 추가할 것: 입금, 출금, 이체, 소명 자료 제출
}
