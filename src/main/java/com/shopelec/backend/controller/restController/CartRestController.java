package com.shopelec.backend.controller.restController;

import com.shopelec.backend.dto.request.CartRequest;
import com.shopelec.backend.dto.response.CartResponse;
import com.shopelec.backend.dto.response.ProductResponse;
import com.shopelec.backend.mapper.ProductMapper;
import com.shopelec.backend.model.Cart;
import com.shopelec.backend.model.Product;
import com.shopelec.backend.repository.ProductRepository;
import com.shopelec.backend.service.cart.CartService;
import com.shopelec.backend.service.product.ProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/v1/api/cart")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class CartRestController {

    CartService cartService;
    ProductMapper productMapper;

    @GetMapping("/{id}")
    public ResponseEntity<?> getAllCartByEmail(@PathVariable Long id) {
        log.info("ID request: {}", id);
        List<CartResponse> carts = cartService.findAllByUserId(id);
        return new ResponseEntity<>(carts, HttpStatus.OK);
    }

    @PostMapping("/addToCart")
    public ResponseEntity<?> addToCart(CartRequest request) {
        log.info(request.toString());
        Cart cart = cartService.findByProductIdAndUserEmail(request.getProduct_id(), request.getEmail());
        if(cart == null) {
            cartService.save(request);
            return new ResponseEntity<>(request, HttpStatus.OK);
        } else {
            CartRequest cartRequest = CartRequest
                    .builder()
                    .id(cart.getId())
                    .product_id(request.getProduct_id())
                    .quantity(cart.getQuantity() + request.getQuantity())
                    .email(request.getEmail())
                    .build();
            cartService.save(cartRequest);
            return new ResponseEntity<>(cartRequest, HttpStatus.OK);

        }
    }

}
