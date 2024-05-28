package com.shopelec.backend.repository;

import com.shopelec.backend.model.ProductSpecification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductSpecificationRepository extends JpaRepository<ProductSpecification, Long> {
    boolean existsByName(String name);
}
