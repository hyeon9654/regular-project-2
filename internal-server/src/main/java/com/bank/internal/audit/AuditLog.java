package com.bank.internal.audit;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// 감사 로그 한 줄. setter 가 없다 → 한 번 만든 기록은 코드로 고칠 수 없다.
@Entity
@Table(name = "audit_log")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_no")
    private String userId;        // 누가 (회원·직원 번호)

    @Column(name = "login_id")
    private String loginId;       // 어떤 아이디로

    @Column(name = "user_role")
    private String role;          // 고객인지 직원인지

    @Column(name = "action_name")
    private String action;        // 무엇을

    @Column(name = "request_path")
    private String path;          // 어떤 주소로 (누구의 것인지 포함)

    @Column(name = "client_ip")
    private String ip;            // 어디서

    @Column(name = "result_code")
    private String result;        // SUCCESS / FAIL

    @Column(name = "created_at")
    private LocalDateTime createdAt;   // 언제 (서버 시계 기준)

    public AuditLog(String userId, String loginId, String role, String action,
                    String path, String ip, String result) {
        this.userId = userId;
        this.loginId = loginId;
        this.role = role;
        this.action = action;
        this.path = path;
        this.ip = ip;
        this.result = result;
        this.createdAt = LocalDateTime.now();
    }
}
