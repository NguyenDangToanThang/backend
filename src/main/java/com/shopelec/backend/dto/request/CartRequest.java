package com.shopelec.backend.dto.request;


import com.shopelec.backend.dto.response.ProductResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartRequest {
    Long id;
    String email;
    Long product_id;
    int quantity = 1;
}
