package com.bank.internal.audit;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// 감사 로그 조회 (직원 전용). 로그를 조회한 것도 로그에 남는다.
// 수정·삭제 API 는 만들지 않는다.
@RestController
@RequestMapping("/staff/audit-logs")
@RequiredArgsConstructor
public class AuditLogController {

    private final AuditLogRepository repository;

    @Audit(action = "AUDIT_LOG_VIEW")
    @GetMapping
    public List<AuditLog> logs() {
        return repository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }
}
