package com.shopelec.backend.repository;

import com.shopelec.backend.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findByUserId(String id);
    Cart findByProductIdAndUserEmail(Long id,String email);
    void deleteAllByUserId(String id);
}
