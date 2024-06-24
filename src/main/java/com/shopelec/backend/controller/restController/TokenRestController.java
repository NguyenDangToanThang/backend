package com.shopelec.backend.controller.restController;

import com.shopelec.backend.service.fcm.DeviceTokenService;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api/tokens")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class TokenRestController {

    DeviceTokenService deviceTokenService;

    @PostMapping("/update")
    public void updateToken(@RequestBody TokenRequest request) {
        deviceTokenService.saveOrUpdateToken(request.getUserId(), request.getToken());
    }

    @Data
    public static class TokenRequest {
        private String userId;
        private String token;
    }
}