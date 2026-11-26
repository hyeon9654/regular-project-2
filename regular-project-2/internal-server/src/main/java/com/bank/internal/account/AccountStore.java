package com.bank.internal.account;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

// 연습용 가짜 계좌 저장소. 실제로는 JPA 리포지토리와 원장 테이블로 바꾼다.
@Component
public class AccountStore {

    private final Map<Long, List<AccountDto>> accounts = Map.of(
            7L, List.of(new AccountDto("111", 50_000), new AccountDto("112", 1_200_000)),
            8L, List.of(new AccountDto("211", 300_000)));

    public List<AccountDto> findByCustomerId(Long customerId) {
        return accounts.getOrDefault(customerId, List.of());
    }
}
