package com.bank.internal.staff;

import com.bank.internal.account.AccountDto;
import com.bank.internal.account.AccountStore;
import com.bank.internal.audit.Audit;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// 직원 업무. 앞으로 이상거래 대응, 소명 심사 등이 이 갈래(/staff/**)에 추가된다.
@RestController
@RequestMapping("/staff")
@RequiredArgsConstructor
public class StaffAccountController {

    private final AccountStore accountStore;

    // 직원이 고객의 계좌를 조회 (조회만 해도 감사 로그에 남는다)
    @Audit(action = "ACCOUNT_VIEW")
    @GetMapping("/customers/{id}/accounts")
    public List<AccountDto> accounts(@PathVariable Long id) {
        return accountStore.findByCustomerId(id);
    }
}
