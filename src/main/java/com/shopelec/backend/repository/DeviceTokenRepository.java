package com.shopelec.backend.repository;

import com.shopelec.backend.model.DeviceTokenFCM;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceTokenRepository extends JpaRepository<DeviceTokenFCM, Long> {
    DeviceTokenFCM findByUserId(String userId);
    DeviceTokenFCM findByToken(String token);
}