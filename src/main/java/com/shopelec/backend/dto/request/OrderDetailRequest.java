package com.shopelec.backend.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDetailRequest {
    Long id;
    @JsonProperty("quantity")
    int quantity;
    @JsonProperty("order_id")
    Long order_id;
    @JsonProperty("product_id")
    Long product_id;
}
