package com.shopelec.backend.service.cart;

import com.shopelec.backend.dto.request.CartRequest;
import com.shopelec.backend.dto.response.CartResponse;
import com.shopelec.backend.dto.response.ProductResponse;
import com.shopelec.backend.dto.response.SpecificationResponse;
import com.shopelec.backend.mapper.ProductMapper;
import com.shopelec.backend.model.Cart;
import com.shopelec.backend.model.Product;
import com.shopelec.backend.model.ProductSpecification;
import com.shopelec.backend.repository.CartRepository;
import com.shopelec.backend.repository.ProductRepository;
import com.shopelec.backend.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartServiceImpl implements CartService{

    private static final Logger log = LoggerFactory.getLogger(CartServiceImpl.class);
    CartRepository cartRepository;
    ProductMapper productMapper;
    UserRepository userRepository;
    ProductRepository productRepository;

    @Override
    public List<CartResponse> findAllByUserId(String id) {
        List<Cart> carts = cartRepository.findByUserId(id);
        List<CartResponse> responses = new ArrayList<>();
        for(Cart cart : carts) {
            ProductResponse productResponse = productMapper.toProductResponse(cart.getProduct());
            productResponse.setImage_url(convertImageUrl(productResponse.getImage_url()));
            List<SpecificationResponse> specificationResponses = new ArrayList<>();
            for(ProductSpecification specification : cart.getProduct().getProductSpecifications()) {
                specificationResponses.add(SpecificationResponse.builder()
                        .id(specification.getId())
                        .description(specification.getDescription())
                        .name(specification.getName())
                        .build());
            }
            productResponse.setSpecifications(specificationResponses);
            responses.add(CartResponse
                    .builder()
                            .id(cart.getId())
                            .productResponse(productResponse)
                            .quantity(cart.getQuantity())
                    .build());
        }
        return responses;
    }

    @Override
    public Cart findByProductIdAndUserEmail(Long id, String email) {
        return cartRepository.findByProductIdAndUserEmail(id,email);
    }

    @Override
    public Cart save(CartRequest request) {
        Product product = productRepository.findById(request.getProduct_id()).orElseThrow(
                () -> new RuntimeException("Product not found")
        );
        Cart cart = Cart
                .builder()
                .id(request.getId())
                .user(userRepository.findByEmail(request.getEmail()).orElseThrow(
                        ()-> new RuntimeException("User not found")
                ))
                .product(product)
                .quantity(request.getQuantity())
                .build();
        return cartRepository.save(cart);
    }

    @Override
    public void setQuantity(int quantity, Long id) {
        Cart cart = cartRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Cart item not found")
        );
        cart.setQuantity(quantity);
        cartRepository.save(cart);
    }

    @Override
    public void deleteByCartId(Long id) {
        cartRepository.deleteById(id);
    }

    private String convertImageUrl(String image_url) {
        return String.format("https://storage.googleapis.com/%s/%s", "shopelec-d93e6.appspot.com", image_url);
    }
}
