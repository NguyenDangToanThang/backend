package com.shopelec.backend.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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

    public LocalDateTime getExpiredDateFormat() {
        try {
            return LocalDateTime.parse(this.getExpiredDate(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        } catch (DateTimeParseException e) {
            DateTimeFormatter alternateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            return LocalDateTime.parse(this.getExpiredDate(), alternateFormatter);
        }
    }

    public long getDiscountFormat() {
        return Double.valueOf(this.discount).longValue();
    }
    public long getDiscountLimitFormat() {
        return Double.valueOf(this.discountLimit).longValue();
    }
}
