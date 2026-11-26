package com.bank.external.auth;

import com.bank.external.security.JwtProvider;
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

// 고객 로그인. 직원 로그인은 이 서버에 없고 internal-server 에 있다.
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    public record LoginRequest(@NotBlank String loginId, @NotBlank String password) {}
    public record Customer(Long id, String password) {}

    // 연습용 가짜 고객. 실제로는 DB에서 찾고, 비밀번호는 BCrypt 해시로 비교한다.
    private final Map<String, Customer> customers = Map.of(
            "kim", new Customer(7L, "1234"),
            "park", new Customer(8L, "1234"));

    private final JwtProvider jwtProvider;

    @PostMapping("/login")
    public Map<String, String> login(@Valid @RequestBody LoginRequest req) {
        Customer customer = customers.get(req.loginId());
        if (customer == null || !customer.password().equals(req.password()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호가 틀림");
        return Map.of("token", jwtProvider.create(customer.id(), req.loginId(), "CUSTOMER"));
    }
}
