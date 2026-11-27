package com.bank.external.core;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

// 내부 서버를 부르는 도구
@Component
public class CoreClient {

    // 내부 서버와 주고받는 데이터 모양. internal-server 의 AccountDto 와 같아야 한다.
    public record AccountDto(String number, long balance) {}

    private final RestClient restClient;

    public CoreClient(@Value("${core.url}") String coreUrl) {
        this.restClient = RestClient.builder().baseUrl(coreUrl).build();
    }

    public List<AccountDto> getAccounts(String customerToken, Long customerId) {
        return restClient.get()
                .uri("/internal/customers/{id}/accounts", customerId)
                .header("Authorization", "Bearer " + customerToken)   // 고객 토큰을 그대로 전달 → 내부 서버가 다시 검증
                .retrieve()
                .body(new ParameterizedTypeReference<List<AccountDto>>() {});
    }
}
