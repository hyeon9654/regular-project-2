package com.bank.internal.audit;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

// 모든 컨트롤러 메서드를 앞뒤로 감싸서, 누가·무엇을·어디로·결과를 기록한다
@Aspect
@Component
@RequiredArgsConstructor
public class AuditAspect {

    private final AuditLogService auditLogService;
    private final HttpServletRequest request;

    @Around("within(@org.springframework.web.bind.annotation.RestController *)")
    public Object record(ProceedingJoinPoint pjp) throws Throwable {
        // 누가 (로그인 전 요청은 anonymous 로 남는다)
        String userId = "anonymous";
        String loginId = "";
        String role = "NONE";
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof Long id) {
            userId = String.valueOf(id);
            loginId = String.valueOf(auth.getDetails());
            role = auth.getAuthorities().iterator().next().getAuthority();
        }

        // 무엇을 (@Audit 이 있으면 그 이름, 없으면 "클래스.메서드")
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Audit audit = signature.getMethod().getAnnotation(Audit.class);
        String action = (audit != null)
                ? audit.action()
                : signature.getDeclaringType().getSimpleName() + "." + signature.getName();

        // 어디로, 어디서
        String path = request.getMethod() + " " + request.getRequestURI();
        String ip = request.getRemoteAddr();

        // 요청 본문은 기록하지 않는다 (비밀번호 등이 로그에 남는 것을 막기 위해)
        try {
            Object result = pjp.proceed();     // 여기서 원래 컨트롤러 메서드가 실행된다
            auditLogService.save(userId, loginId, role, action, path, ip, "SUCCESS");
            return result;
        } catch (Throwable e) {
            auditLogService.save(userId, loginId, role, action, path, ip, "FAIL");
            throw e;
        }
    }
}
