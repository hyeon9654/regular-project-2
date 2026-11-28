package com.bank.external.auth;

import com.bank.external.auth.dto.CustomerLoginRequest;
import com.bank.external.common.exception.BusinessException;
import com.bank.external.security.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@RequiredArgsConstructor

public class CustomerAuthService {
    private final DeviceRepository deviceRepository;
    private final CredentialRepository credentialRepository ;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @Transactional(noRollbackFor = BusinessException.class) // BusinessException이 나면 롤백하지 말고 저장
    public String login(CustomerLoginRequest request){
        // 1. 기기 찾기 -> 없으면 에러
        DeviceEntity device = deviceRepository.findByDeviceUuid(request.deviceUuid())
                .filter(deviceEntity -> deviceEntity.isActive()) // 해제된 기기인지 확인
                .orElseThrow(() -> new BusinessException(AuthErrorCode.DEVICE_NOT_REGISTERED));

        // 기기 주인의 회원 번호
        Long memberId = device.getMember().getMemberId();

        // 2. 간편비밀번호 찾기 -> 없으면 에러
        CredentialEntity credential = credentialRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(AuthErrorCode.DEVICE_NOT_REGISTERED));

        // 3. 잠겼는지
        if (credential.isLocked()){
            throw new BusinessException(AuthErrorCode.PIN_LOCKED);
        }

        // 4. PIN 비교
        if (!passwordEncoder.matches(request.pin(), credential.getPinHash())){
            credential.recordFailure(); // PIN 비교해서 다르면 틀린 횟수 +1
            if (credential.isLocked()){ // +1 이라 바로 5회 체크
                throw new BusinessException(AuthErrorCode.PIN_LOCKED);
            }
            throw new BusinessException(AuthErrorCode.PIN_MISMATCH);
        }

        // 5. 성공 -> 토큰 발급
        credential.resetFailures(); // 성공 했으니 기회 0으로 초기화
        return tokenService.createAccessToken(memberId, "CUSTOMER");
    }
}
