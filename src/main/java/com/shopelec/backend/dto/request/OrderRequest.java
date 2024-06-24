package com.shopelec.backend.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.shopelec.backend.model.Address;
import com.shopelec.backend.model.Coupons;
import com.shopelec.backend.model.OrderDetail;
import com.shopelec.backend.model.User;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderRequest {
    Long id;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime orderDate;
    double totalPrice;
    String status;
    Long coupons_id;
    String user_id;
    Long address_id;
    List<OrderDetailRequest> orderDetailRequests;
}
