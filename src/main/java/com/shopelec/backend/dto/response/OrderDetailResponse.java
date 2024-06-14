package com.shopelec.backend.dto.response;

import com.shopelec.backend.model.Order;
import com.shopelec.backend.model.Product;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDetailResponse {
    Long id;
    int quantity;
    ProductResponse productResponse;
}
