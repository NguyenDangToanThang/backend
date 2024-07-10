package com.shopelec.backend.service.order;

import com.shopelec.backend.dto.request.OrderDetailRequest;
import com.shopelec.backend.dto.request.OrderRequest;
import com.shopelec.backend.dto.response.AddressResponse;
import com.shopelec.backend.dto.response.OrderResponse;
import com.shopelec.backend.model.*;
import com.shopelec.backend.repository.*;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
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
                .modifiedDate(LocalDateTime.now())
                .build());

        List<OrderDetailRequest> orderDetails = request.getOrderDetailRequests();
        for(OrderDetailRequest request1 : orderDetails) {
            Product product = productRepository.findById(request1.getProduct_id()).orElseThrow(
                    () -> new RuntimeException("Product not found")
            );
            product.setQuantity(product.getQuantity() - request1.getQuantity());
            productRepository.save(product);
            detailRepository.save(OrderDetail.builder()
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
        List<Order> orders = orderRepository.findAllOrderByUserIdOrderByOrderDateDesc(user_id);
        for(Order order : orders) {
           if(order.getStatus().trim().equals(status.trim())) {
               Coupons coupons = Coupons.builder()
                       .code("")
                       .id(-1L)
                       .description("")
                       .discount(0)
                       .discountLimit(0)
                       .expiredDate(new Date())
                       .quantity(0)
                       .build();
               if(order.getCoupon_id() != -1) {
                    coupons = couponsRepository.findById(order.getCoupon_id()).orElseThrow(
                           () -> new RuntimeException("Coupon not found")
                   );
               }
               responses.add(OrderResponse.builder()
                       .id(order.getId())
                       .orderDate(order.getOrderDate())
                       .totalPrice(order.getTotalPrice())
                       .status(order.getStatus())
                       .modifiedDate(order.getModifiedDate())
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
    public void update(Long id, String status) {
        Order order = findById(id);
        order.setStatus(status);
        order.setModifiedDate(LocalDateTime.now());
        orderRepository.save(order);
    }

    @Override
    public List<Order> getAllOrder() {
        return orderRepository.findAll();
    }

    @Override
    public Page<Order> getAllOrdersPaginatedStatus(String status, Pageable pageable) {
        return orderRepository.findByStatus(status, pageable);
    }

    @Override
    public Page<Order> getAllOrdersPaginated(Pageable pageable) {
        return orderRepository.findAll(pageable);

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
        if(!Objects.equals(status, "Đã hủy") && !Objects.equals(status, "Chờ duyệt")) {
            order.setModifiedDate(LocalDateTime.now());
            orderRepository.save(order);
            return;
        }
        if(Objects.equals(status, "Đã hủy")) {
            Coupons coupons = couponsRepository.findById(order.getCoupon_id()).orElse(null);
            if(coupons != null && coupons.getId() != -1) {
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
            order.setModifiedDate(LocalDateTime.now());
            orderRepository.save(order);
        }
        else if (Objects.equals(status, "Chờ duyệt")) {
            order.setStatus("Đã hủy");
            List<OrderDetail> orderDetails = detailRepository.findAllByOrderId(order.getId());
            for (OrderDetail orderDetail : orderDetails) {
                Product product = productRepository.findById(orderDetail.getProduct().getId()).orElseThrow(
                        () -> new RuntimeException("Product not found")
                );
                product.setQuantity(product.getQuantity() - orderDetail.getQuantity());
                productRepository.save(product);
            }
            Order order1 = Order.builder()
                    .address(order.getAddress())
                    .user(order.getUser())
                    .status(status)
                    .orderDate(LocalDateTime.now())
                    .coupon_id(order.getCoupon_id())
                    .totalPrice(order.getTotalPrice())
                    .build();
            List<OrderDetail> orderDetails1 = new ArrayList<>();
            for (OrderDetail orderDetail : orderDetails) {
                orderDetails1.add(OrderDetail.builder()
                                .product(orderDetail.getProduct())
                                .quantity(orderDetail.getQuantity())
                                .order(orderRepository.save(order1))
                                .status(false)
                        .build());
            }

            detailRepository.saveAll(orderDetails1);
        }
    }
}
