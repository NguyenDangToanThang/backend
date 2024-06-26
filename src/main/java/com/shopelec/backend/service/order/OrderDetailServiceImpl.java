package com.shopelec.backend.service.order;

import com.shopelec.backend.dto.response.OrderDetailRatingResponse;
import com.shopelec.backend.dto.response.OrderDetailResponse;
import com.shopelec.backend.dto.response.ProductResponse;
import com.shopelec.backend.model.OrderDetail;
import com.shopelec.backend.repository.OrderDetailRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public class OrderDetailServiceImpl implements OrderDetailService{

    OrderDetailRepository detailRepository;

    @Override
    public List<OrderDetailRatingResponse> getAllOrderDetailByOrderId(Long order_id) {
        List<OrderDetail> details = detailRepository.findAllByOrderId(order_id);
        List<OrderDetailRatingResponse> responses = new ArrayList<>();
        for(OrderDetail orderDetail : details) {
            responses.add(OrderDetailRatingResponse.builder()
                    .id(orderDetail.getId())
                    .quantity(orderDetail.getQuantity())
                    .productId(orderDetail.getProduct().getId())
                    .imageUrl(orderDetail.getProduct().getImageLink())
                    .name(orderDetail.getProduct().getName())
                    .price(orderDetail.getProduct().getPrice())
                    .discount(orderDetail.getProduct().getDiscount())
                    .status(orderDetail.isStatus())
                    .build());
        }
        return responses;
    }

    @Override
    public List<OrderDetailResponse> getAllOrderDetailByOrder(Long order_id) {

        List<OrderDetail> details = detailRepository.findAllByOrderId(order_id);
        List<OrderDetailResponse> responses = new ArrayList<>();
        for(OrderDetail orderDetail : details) {
            responses.add(OrderDetailResponse.builder()
                    .id(orderDetail.getId())
                    .quantity(orderDetail.getQuantity())
                    .productResponse(ProductResponse.builder()
                            .id(orderDetail.getProduct().getId())
                            .price(orderDetail.getProduct().getPrice())
                            .discount(orderDetail.getProduct().getDiscount())
                            .name(orderDetail.getProduct().getName())
                            .image_url(orderDetail.getProduct().getImage_url())
                            .build()
                    )
                    .build());
        }
        return responses;


    }

    @Override
    public void updateStatus(Long productId, Long orderId) {
        OrderDetail orderDetail = detailRepository.findByProductIdAndOrderId(productId,orderId);
        orderDetail.setStatus(true);
        detailRepository.save(orderDetail);
    }
}




