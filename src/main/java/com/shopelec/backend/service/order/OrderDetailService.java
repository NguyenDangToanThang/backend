package com.shopelec.backend.service.order;

import com.shopelec.backend.dto.response.OrderDetailRatingResponse;
import com.shopelec.backend.dto.response.OrderDetailResponse;
import com.shopelec.backend.model.OrderDetail;

import java.util.List;

public interface OrderDetailService {
    List<OrderDetailRatingResponse> getAllOrderDetailByOrderId(Long order_id);
    List<OrderDetailResponse> getAllOrderDetailByOrder(Long order_id);
    void updateStatus(Long productId,Long orderId);
}
