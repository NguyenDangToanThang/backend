package com.shopelec.backend.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReviewRequest {
    Long id;
    double rate;
    String comment;
    Long productId;
    Long orderId;
    String userId;
}
