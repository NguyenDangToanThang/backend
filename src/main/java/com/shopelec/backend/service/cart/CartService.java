package com.shopelec.backend.service.cart;

import com.shopelec.backend.dto.request.CartRequest;
import com.shopelec.backend.dto.response.CartResponse;
import com.shopelec.backend.model.Cart;

import java.util.List;

public interface CartService {
    List<CartResponse> findAllByUserId(String id);
    Cart findByProductIdAndUserEmail(Long id, String email);
    Cart save(CartRequest request);
    void setQuantity(int quantity, Long id);
}
