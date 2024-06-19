package com.shopelec.backend.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class CouponsResponse {
    Long id;
    String code;
    String description;
    double discount;
    double discountLimit;
    String expiredDate;
    int quantity;
    String status;
}
