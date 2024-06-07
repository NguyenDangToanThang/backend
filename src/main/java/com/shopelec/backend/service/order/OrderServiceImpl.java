package com.shopelec.backend.service.order;

import com.shopelec.backend.dto.request.OrderRequest;
import com.shopelec.backend.dto.response.AddressResponse;
import com.shopelec.backend.dto.response.OrderResponse;
import com.shopelec.backend.model.Order;
import com.shopelec.backend.model.User;
import com.shopelec.backend.repository.AddressRepository;
import com.shopelec.backend.repository.OrderRepository;
import com.shopelec.backend.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class OrderServiceImpl implements OrderService{

    OrderRepository orderRepository;
    AddressRepository addressRepository;
    UserRepository userRepository;

    @Override
    public OrderResponse save(OrderRequest request) {
        User user = userRepository.findById(request.getUser_id()).orElseThrow(
                () -> new RuntimeException("User not found")
        );
        Order order = orderRepository.save(Order
                .builder()
                .address(addressRepository.findById(request.getAddress_id()).orElseThrow(
                        () -> new RuntimeException("Address not found")
                ))
                .user(user)
                .orderDate(request.getOrderDate())
                .status(request.getStatus())
                .totalPrice(request.getTotalPrice())
                .build());
        return OrderResponse.builder()
                .id(order.getId())
                .orderDate(order.getOrderDate())
                .totalPrice(order.getTotalPrice())
                .status(order.getStatus())
                .addressResponse(
                        AddressResponse.builder()
                                .name(order.getAddress().getName())
                                .user_id(order.getAddress().getUser().getId())
                                .phoneNumber(order.getAddress().getPhoneNumber())
                                .address(order.getAddress().getAddress())
                                .build()
                )
                .build();
    }

    @Override
    public List<OrderResponse> getAllOrderByUserId(Long user_id) {
        List<OrderResponse> responses = new ArrayList<>();
        List<Order> orders = orderRepository.findAllOrderByUserId(user_id);
        for(Order order : orders) {
            responses.add(OrderResponse.builder()
                    .id(order.getId())
                    .orderDate(order.getOrderDate())
                    .totalPrice(order.getTotalPrice())
                    .status(order.getStatus())
                    .addressResponse(
                            AddressResponse.builder()
                                    .name(order.getAddress().getName())
                                    .user_id(order.getAddress().getUser().getId())
                                    .phoneNumber(order.getAddress().getPhoneNumber())
                                    .address(order.getAddress().getAddress())
                                    .build()
                    )
                    .build());
        }
        return responses;
    }

    @Override
    public OrderResponse update(OrderRequest request) {
        return null;
    }
}
