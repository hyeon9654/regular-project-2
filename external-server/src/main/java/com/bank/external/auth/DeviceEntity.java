package com.bank.external.auth;

import com.bank.external.member.MemberEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "device")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeviceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long deviceId;

    @ManyToOne(fetch = FetchType.LAZY)                      // 회원 1명 : 기기 여러 줄, 회원은 꺼낼 때만 읽음
    @JoinColumn(name = "member_id", nullable = false)       // FK 컬럼, 주인 없는 기기는 없음
    private MemberEntity member;                            // 이 기기의 주인

    @Column(nullable = false, unique = true, length = 100)
    private String deviceFingerprint;                       // 앱이 만든 기기 식별값

    @Column(nullable = false)
    private LocalDateTime registeredAt;                     // 등록한 시간

    private LocalDateTime revokedAt;                        // 등록을 해제한 시간
}
