package com.shopelec.backend.repository;

import com.shopelec.backend.model.Product;
import com.shopelec.backend.model.ProductSpecification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductSpecificationRepository extends JpaRepository<ProductSpecification, Long> {
    boolean existsByNameAndProductId(String name, Long productId);
    ProductSpecification findByNameAndProductId(String name, Long productId);
    List<ProductSpecification> findAllByProductId(Long productId);
}
