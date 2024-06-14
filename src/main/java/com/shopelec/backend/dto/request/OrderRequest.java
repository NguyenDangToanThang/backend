package com.shopelec.backend.dto.request;

import com.shopelec.backend.model.Address;
import com.shopelec.backend.model.OrderDetail;
import com.shopelec.backend.model.User;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderRequest {
    Long id;
    Date orderDate;
    double totalPrice;
    String status;
    String user_id;
    Long address_id;
    List<OrderDetail> orderDetails;
}
