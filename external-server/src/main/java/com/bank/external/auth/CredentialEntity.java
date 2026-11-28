package com.bank.external.auth;

import com.bank.external.member.MemberEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "credential")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CredentialEntity {
    @Id
    private Long memberId;          // PK. 값은 아래 member에서 가져온다

    @MapsId                           // PK 값 = 연결된 회원의 PK로 맞춘다
    @OneToOne(fetch = FetchType.LAZY) // 회원 1명 비밀번호 1줄
    @JoinColumn(name = "member_id")   // FK 컬럼
    private MemberEntity member;      // 이 비밀번호의 주인

    @Column(nullable = false)
    private String pinHash;         // PIN값 BCrypt 해시

    @Column(nullable = false)
    private int failedCount;        // 연속으로 틀린 횟수

    private LocalDateTime lockedAt; // 잠긴 시각
}
