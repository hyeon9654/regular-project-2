package com.bank.internal.customer;

import com.bank.internal.account.AccountDto;
import com.bank.internal.account.AccountStore;
import com.bank.internal.audit.Audit;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

// 외부 서버가 고객을 대신해 호출하는 주소
@RestController
@RequestMapping("/internal")
@RequiredArgsConstructor
public class InternalAccountController {

    private final AccountStore accountStore;

    @Audit(action = "MY_ACCOUNT_VIEW")
    @GetMapping("/customers/{id}/accounts")
    public List<AccountDto> accounts(@AuthenticationPrincipal Long userId, @PathVariable Long id) {
        // 외부 서버가 넘긴 번호를 그대로 믿지 않고, 토큰의 주인과 같은지 내부 서버가 직접 확인한다
        if (!id.equals(userId))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "본인 계좌만 조회할 수 있음");
        return accountStore.findByCustomerId(id);
    }
}
