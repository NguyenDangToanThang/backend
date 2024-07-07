package com.shopelec.backend.repository;

import com.shopelec.backend.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllOrderByUserIdOrderByOrderDateDesc(String user_id);
    Page<Order> findByStatus(String status, Pageable pageable);

}
