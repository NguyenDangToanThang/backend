package com.shopelec.backend.service.review;

import com.shopelec.backend.dto.request.ReviewRequest;
import com.shopelec.backend.model.Order;
import com.shopelec.backend.model.Product;
import com.shopelec.backend.model.Review;
import com.shopelec.backend.model.User;
import com.shopelec.backend.repository.OrderRepository;
import com.shopelec.backend.repository.ProductRepository;
import com.shopelec.backend.repository.ReviewRepository;
import com.shopelec.backend.repository.UserRepository;
import com.shopelec.backend.service.order.OrderDetailService;
import com.shopelec.backend.service.order.OrderService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    ReviewRepository reviewRepository;
    ProductRepository productRepository;
    UserRepository userRepository;
    OrderRepository orderRepository;
    OrderDetailService detailService;

    @Override
    public void save(ReviewRequest request) {

        Product product = productRepository.findById(request.getProductId()).orElseThrow(
                () -> new RuntimeException("Product not found")
        );
        Order order = orderRepository.findById(request.getOrderId()).orElseThrow(
                () -> new RuntimeException("Order not found")
        );
        User user = userRepository.findById(request.getUserId()).orElseThrow(
                () -> new RuntimeException("User not found")
        );

        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = dateFormat.format(date);

        reviewRepository.save(Review.builder()
                        .user(user)
                        .product(product)
                        .comment(request.getComment())
                        .date_created(formattedDate)
                        .rate(request.getRate())
                .build());

        detailService.updateStatus(product.getId(), order.getId());

    }
}
