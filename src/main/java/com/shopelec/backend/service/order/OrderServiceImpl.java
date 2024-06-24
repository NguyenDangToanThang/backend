package com.shopelec.backend.service.order;

import com.shopelec.backend.dto.request.OrderDetailRequest;
import com.shopelec.backend.dto.request.OrderRequest;
import com.shopelec.backend.dto.response.AddressResponse;
import com.shopelec.backend.dto.response.OrderResponse;
import com.shopelec.backend.mapper.UserMapper;
import com.shopelec.backend.model.*;
import com.shopelec.backend.repository.*;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class OrderServiceImpl implements OrderService{

    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);
    OrderRepository orderRepository;
    OrderDetailRepository detailRepository;
    AddressRepository addressRepository;
    UserRepository userRepository;
    CouponsRepository couponsRepository;
    ProductRepository productRepository;
    CartRepository cartRepository;
    UserMapper userMapper;

    @Transactional
    @Override
    public boolean save(OrderRequest request) {
        User user = userRepository.findById(request.getUser_id()).orElseThrow(
                () -> new RuntimeException("User not found")
        );
        if(request.getCoupons_id() != -1) {
            Coupons coupons = couponsRepository.findById(request.getCoupons_id()).orElseThrow(
                    () -> new RuntimeException("Coupons not found")
            );
            coupons.setQuantity(coupons.getQuantity() - 1);
            couponsRepository.save(coupons);
        }
        Order order = orderRepository.save(Order
                .builder()
                .address(addressRepository.findById(request.getAddress_id()).orElseThrow(
                        () -> new RuntimeException("Address not found")
                ))
                .user(user)
                .coupon_id(request.getCoupons_id())
                .orderDate(request.getOrderDate())
                .status(request.getStatus())
                .totalPrice(request.getTotalPrice())
                .build());

        List<OrderDetailRequest> orderDetails = request.getOrderDetailRequests();
        for(OrderDetailRequest request1 : orderDetails) {
            Product product = productRepository.findById(request1.getProduct_id()).orElseThrow(
                    () -> new RuntimeException("Product not found")
            );
            product.setQuantity(product.getQuantity() - request1.getQuantity());
            productRepository.save(product);
            OrderDetail detail = detailRepository.save(OrderDetail.builder()
                            .product(product)
                            .quantity(request1.getQuantity())
                            .order(order)
                    .build());
        }
        cartRepository.deleteAllByUserId(user.getId());
        return true;
    }

    @Override
    public List<OrderResponse> getAllOrderByUserId(String user_id,String status) {
        List<OrderResponse> responses = new ArrayList<>();
        List<Order> orders = orderRepository.findAllOrderByUserId(user_id);
        for(Order order : orders) {
           if(order.getStatus().trim().equals(status.trim())) {
               Coupons coupons = couponsRepository.findById(order.getCoupon_id()).orElseThrow(
                       () -> new RuntimeException("Coupon not found")
               );
               responses.add(OrderResponse.builder()
                       .id(order.getId())
                       .orderDate(order.getOrderDate())
                       .totalPrice(order.getTotalPrice())
                       .status(order.getStatus())
                       .addressResponse(
                               AddressResponse.builder()
                                       .id(order.getAddress().getId())
                                       .name(order.getAddress().getName())
                                       .user_id(order.getAddress().getUser().getId())
                                       .phoneNumber(order.getAddress().getPhoneNumber())
                                       .address(order.getAddress().getAddress())
                                       .build())
                       .coupons(coupons)
                       .build());
           }
        }
        return responses;
    }

    @Override
    public OrderResponse update(OrderRequest request) {
        return null;
    }

    @Override
    public List<Order> getAllOrder() {
        return orderRepository.findAll();
    }

    @Override
    public Order findById(Long id) {
        return orderRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Order not found")
        );
    }

    @Override
    public void updateStatus(Long id, String status) {
        Order order = findById(id);
        order.setStatus(status);
        if(Objects.equals(status, "Đã hủy")) {

            Coupons coupons = couponsRepository.findById(order.getCoupon_id()).orElseThrow(
                    () -> new RuntimeException("Coupons not found")
            );
            if(coupons.getQuantity() - 1 > 0 || coupons.getId() != -1) {
                coupons.setQuantity(coupons.getQuantity() - 1);
            }
            List<OrderDetail> orderDetails = detailRepository.findAllByOrderId(order.getId());
            for(int i = 0 ; i < orderDetails.size() - 1 ; i++) {
                Product product = productRepository.findById(orderDetails.get(i).getProduct().getId()).orElseThrow(
                        () -> new RuntimeException("Product not found")
                );
                product.setQuantity(product.getQuantity() + orderDetails.get(i).getQuantity());
                productRepository.save(product);
            }
            Order order1 = orderRepository.save(order);
            log.info("Order 1: {}", order1.getId());
        }
        else if (Objects.equals(status, "Chờ duyệt")) {
            Coupons coupons = couponsRepository.findById(order.getCoupon_id()).orElseThrow(
                    () -> new RuntimeException("Coupons not found")
            );
            if (coupons.getId() != -1) {
                coupons.setQuantity(coupons.getQuantity() + 1);
            }

            List<OrderDetail> orderDetails = detailRepository.findAllByOrderId(order.getId());
            for (OrderDetail orderDetail : orderDetails) {
                Product product = productRepository.findById(orderDetail.getProduct().getId()).orElseThrow(
                        () -> new RuntimeException("Product not found")
                );
                product.setQuantity(product.getQuantity() - orderDetail.getQuantity());
                productRepository.save(product);
            }
            order.setOrderDate(LocalDateTime.now());
            orderRepository.save(order);

        }
    }
}
