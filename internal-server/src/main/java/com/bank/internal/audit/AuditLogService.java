package com.bank.internal.audit;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuditLogService {

    private final AuditLogRepository repository;

    // REQUIRES_NEW: 업무가 실패해 롤백돼도 "실패했다"는 기록은 따로 저장되어 남는다
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void save(String userId, String loginId, String role, String action,
                     String path, String ip, String result) {
        repository.save(new AuditLog(userId, loginId, role, action, path, ip, result));
    }
}
