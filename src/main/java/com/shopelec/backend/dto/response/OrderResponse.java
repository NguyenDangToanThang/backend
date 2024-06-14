package com.shopelec.backend.dto.response;

import com.shopelec.backend.model.OrderDetail;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderResponse {
    Long id;
    Date orderDate;
    double totalPrice;
    String status;
    AddressResponse addressResponse;
}
