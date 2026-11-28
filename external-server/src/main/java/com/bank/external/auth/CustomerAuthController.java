package com.bank.external.auth;

import com.bank.external.auth.dto.CustomerLoginRequest;
import com.bank.external.auth.dto.CustomerLoginResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/customer")
public class CustomerAuthController {
    private final CustomerAuthService customerAuthService;

    @PostMapping("/login")
    public CustomerLoginResponse login(@Valid @RequestBody CustomerLoginRequest request){
        String accessToken = customerAuthService.login(request);
        return new CustomerLoginResponse(accessToken);
    }
}
