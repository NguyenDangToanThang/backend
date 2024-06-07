package com.shopelec.backend.service.order;

import com.shopelec.backend.dto.request.OrderRequest;
import com.shopelec.backend.dto.response.OrderResponse;

import java.util.List;

public interface OrderService {
    OrderResponse save(OrderRequest request);
    List<OrderResponse> getAllOrderByUserId(Long user_id);
    OrderResponse update(OrderRequest request);
}
