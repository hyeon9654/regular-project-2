package com.bank.internal.account;

// 외부 서버와 주고받는 데이터 모양. external-server 의 CoreClient.AccountDto 와 같아야 한다.
public record AccountDto(String number, long balance) {}
