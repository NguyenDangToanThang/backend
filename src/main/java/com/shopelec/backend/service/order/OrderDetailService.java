package com.shopelec.backend.service.order;

import com.shopelec.backend.dto.response.OrderDetailResponse;
import com.shopelec.backend.model.OrderDetail;

import java.util.List;

public interface OrderDetailService {
    List<OrderDetailResponse> getAllOrderDetailByOrderId(Long order_id);
}
