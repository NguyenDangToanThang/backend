package com.shopelec.backend.repository;

import com.shopelec.backend.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllOrderByUserId(Long user_id);
}
