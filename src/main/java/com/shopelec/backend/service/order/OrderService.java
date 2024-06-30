package com.shopelec.backend.service.order;

import com.shopelec.backend.dto.request.OrderRequest;
import com.shopelec.backend.dto.response.OrderResponse;
import com.shopelec.backend.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {
    boolean save(OrderRequest request);
    List<OrderResponse> getAllOrderByUserId(String user_id, String status);
    void update(Long id, String status);
    List<Order> getAllOrder();
    Page<Order> getAllOrdersPaginatedStatus(String status , Pageable pageable);
    Page<Order> getAllOrdersPaginated(Pageable pageable);
    Order findById(Long id);
    void updateStatus(Long id, String status);
}
