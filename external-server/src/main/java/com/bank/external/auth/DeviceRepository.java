package com.bank.external.auth;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeviceRepository extends JpaRepository<DeviceEntity, Long> {
    Optional<DeviceEntity> findByDeviceUuid(String deviceUuid); // deviceUuid로 DeviceEntity 찾기
}
