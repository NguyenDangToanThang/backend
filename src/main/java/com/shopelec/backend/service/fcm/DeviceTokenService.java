package com.shopelec.backend.service.fcm;

import com.shopelec.backend.model.DeviceTokenFCM;
import com.shopelec.backend.repository.DeviceTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DeviceTokenService {

    @Autowired
    private DeviceTokenRepository deviceTokenRepository;

    public void saveOrUpdateToken(String userId, String token) {
        Optional<DeviceTokenFCM> existingToken = Optional.ofNullable(deviceTokenRepository.findByUserId(userId));
        if (existingToken.isPresent()) {
            DeviceTokenFCM tokenEntity = existingToken.get();
            tokenEntity.setToken(token);
            deviceTokenRepository.save(tokenEntity);
        } else {
            DeviceTokenFCM newToken = new DeviceTokenFCM();
            newToken.setUserId(userId);
            newToken.setToken(token);
            deviceTokenRepository.save(newToken);
        }
    }

    public DeviceTokenFCM getTokenByUserId(String userId) {
        return deviceTokenRepository.findByUserId(userId);
    }

    public DeviceTokenFCM getTokenByToken(String token) {
        return deviceTokenRepository.findByToken(token);
    }
}