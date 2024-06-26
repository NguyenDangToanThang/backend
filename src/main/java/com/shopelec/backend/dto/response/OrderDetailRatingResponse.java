package com.shopelec.backend.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDetailRatingResponse {
    Long id;
    int quantity;
    Long productId;
    String name;
    double price;
    String imageUrl;
    int discount;
    boolean status;
}
