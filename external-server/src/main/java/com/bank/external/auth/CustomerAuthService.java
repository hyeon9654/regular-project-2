package com.bank.external.auth;

import com.bank.external.security.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor

public class CustomerAuthService {
    private final DeviceRepository deviceRepository;
    private final CredentialRepository credentialRepository ;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;


}
