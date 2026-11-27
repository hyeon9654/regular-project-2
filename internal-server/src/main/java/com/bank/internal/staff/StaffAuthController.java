package com.bank.internal.staff;

import com.bank.internal.security.JwtProvider;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

// 직원 로그인. 이 주소는 내부 서버에만 있어서, 배포 후에는 내부망(VPN)에서만 닿는다.
@RestController
@RequestMapping("/staff/auth")
@RequiredArgsConstructor
public class StaffAuthController {

    public record LoginRequest(@NotBlank String loginId, @NotBlank String password) {}
    public record Staff(Long id, String password) {}

    // 연습용 가짜 직원. 실제로는 DB에서 찾고, 비밀번호는 BCrypt 해시로 비교한다.
    private final Map<String, Staff> staffs = Map.of(
            "lee", new Staff(9L, "1234"));

    private final JwtProvider jwtProvider;

    @PostMapping("/login")
    public Map<String, String> login(@Valid @RequestBody LoginRequest req) {
        Staff staff = staffs.get(req.loginId());
        if (staff == null || !staff.password().equals(req.password()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호가 틀림");
        return Map.of("token", jwtProvider.create(staff.id(), req.loginId(), "STAFF"));
    }
}
