package com.shopelec.backend.dto.response;

import com.shopelec.backend.model.Coupons;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderResponse {
    Long id;
    LocalDateTime orderDate;
    double totalPrice;
    String status;
    AddressResponse addressResponse;
    Coupons coupons;
}
