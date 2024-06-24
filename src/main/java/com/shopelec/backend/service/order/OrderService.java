package com.shopelec.backend.service.order;

import com.shopelec.backend.dto.request.OrderRequest;
import com.shopelec.backend.dto.response.OrderResponse;
import com.shopelec.backend.model.Order;

import java.util.List;

public interface OrderService {
    boolean save(OrderRequest request);
    List<OrderResponse> getAllOrderByUserId(String user_id, String status);
    OrderResponse update(OrderRequest request);
    List<Order> getAllOrder();
    Order findById(Long id);
    void updateStatus(Long id, String status);
}
